package protocolsupport.protocol;

import java.net.InetSocketAddress;
import java.text.MessageFormat;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import io.netty.util.Recycler;
import io.netty.util.Recycler.Handle;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.connection.UpstreamBridge;
import net.md_5.bungee.netty.PacketHandler;
import protocolsupport.api.Connection;
import protocolsupport.api.Connection.PacketListener.PacketEvent;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.injector.BungeeNettyChannelInjector.CustomHandlerBoss;
import protocolsupport.protocol.packet.handler.PSInitialHandler;
import protocolsupport.protocol.pipeline.ChannelHandlers;
import protocolsupport.protocol.storage.ProtocolStorage;
import protocolsupport.utils.ReflectionUtils;

public class ConnectionImpl extends Connection {

	protected static final AttributeKey<ConnectionImpl> key = AttributeKey.valueOf("PSConnectionImpl");

	protected final CustomHandlerBoss boss;
	protected final PSInitialHandler initial;
	public ConnectionImpl(CustomHandlerBoss boss, PSInitialHandler initial) {
		this.boss = boss;
		this.initial = initial;
	}

	@Override
	public Object getNetworkManager() {
		return boss;
	}

	@Override
	public boolean isConnected() {
		return initial.getChannelWrapper().getHandle().isOpen();
	}

	@Override
	public InetSocketAddress getRawAddress() {
		return (InetSocketAddress) initial.getChannelWrapper().getHandle().remoteAddress();
	}

	@Override
	public InetSocketAddress getAddress() {
		return initial.getAddress();
	}

	@Override
	public void changeAddress(InetSocketAddress newRemote) {
		ProtocolStorage.addAddress(getRawAddress(), newRemote);
		initial.getChannelWrapper().setRemoteAddress(newRemote);
	}

	@Override
	public ProxiedPlayer getPlayer() {
		PacketHandler handler = boss.getHandler();
		if (handler instanceof UpstreamBridge) {
			try {
				return ReflectionUtils.getFieldValue(handler, "con");
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
		}
		return null;
	}

	public static ConnectionImpl getFromChannel(Channel channel) {
		return channel.attr(key).get();
	}

	public void storeInChannel(Channel channel) {
		channel.attr(key).set(this);
	}

	public void setVersion(ProtocolVersion version) {
		this.version = version;
	}

	protected Channel serverConnectionChannel;

	public void setServerConnectionChannel(Channel serverConnectionChannel) {
		this.serverConnectionChannel = serverConnectionChannel;
	}

	@Override
	public void sendPacketToClient(Object packet) {
		Channel clientConnectionChannel = initial.getChannelWrapper().getHandle();
		runTask(clientConnectionChannel, () -> {
			try {
				clientConnectionChannel.pipeline().context(ChannelHandlers.LOGIC).writeAndFlush(packet);
			} catch (Throwable t) {
				System.err.println("Error occured while packet sending to client");
				t.printStackTrace();
			}
		});
	}

	@Override
	public void sendPacketToServer(Object packet) {
		if (serverConnectionChannel != null) {
			runTask(serverConnectionChannel, () -> {
				try {
					serverConnectionChannel.pipeline().context(ChannelHandlers.LOGIC).writeAndFlush(packet);
				} catch (Throwable t) {
					System.err.println("Error occured while packet sending to server");
					t.printStackTrace();
				}
			});
		}
	}

	protected void runTask(Channel channel, Runnable task) {
		if (channel.eventLoop().inEventLoop()) {
			task.run();
		} else {
			channel.eventLoop().submit(task);
		}
	}

	protected static class LPacketEvent extends PacketEvent implements AutoCloseable {

		protected static final Recycler<LPacketEvent> recycler = new Recycler<LPacketEvent>() {
			@Override
			protected LPacketEvent newObject(Handle<LPacketEvent> handle) {
				return new LPacketEvent(handle);
			}
		};

		public static LPacketEvent create(Object packet) {
			LPacketEvent packetevent = recycler.get();
			packetevent.packet = packet;
			packetevent.cancelled = false;
			return packetevent;
		}

		protected final Handle<LPacketEvent> handle;
		protected LPacketEvent(Handle<LPacketEvent> handle) {
			this.handle = handle;
		}

		public void recycle() {
			this.handle.recycle(this);
		}

		@Override
		public void close() {
			recycle();
		}

	}

	public Object handlePacketSend(Object packet, boolean isClientConnection) {
		try (LPacketEvent packetevent = LPacketEvent.create(packet)) {
			for (PacketListener listener : isClientConnection ? clientConnectionPacketListeners : serverConnectionPacketListeners) {
				try {
					listener.onPacketSending(packetevent);
				} catch (Throwable t) {
					System.err.println("Error occured while handling packet sending");
					t.printStackTrace();
				}
			}
			return packetevent.isCancelled() ? null : packetevent.getPacket();
		}
	}

	public Object handlePacketReceive(Object packet, boolean isClientConnection) {
		try (LPacketEvent packetevent = LPacketEvent.create(packet)) {
			for (PacketListener listener : isClientConnection ? clientConnectionPacketListeners : serverConnectionPacketListeners) {
				try {
					listener.onPacketReceiving(packetevent);
				} catch (Throwable t) {
					System.err.println("Error occured while handling packet receiving");
					t.printStackTrace();
				}
			}
			return packetevent.isCancelled() ? null : packetevent.getPacket();
		}
	}

	@Override
	public String toString() {
		return MessageFormat.format(
			"{0}(player: {1}, address: {2}, rawaddress: {3}, version: {4}, metadata: {5})",
			getClass().getName(), getPlayer(), getAddress(), getRawAddress(), getVersion(), metadata
		);
	}

}
