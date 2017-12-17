package protocolsupport.protocol;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.text.MessageFormat;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.UpstreamBridge;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.PacketHandler;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.injector.BungeeNettyChannelInjector.CustomHandlerBoss;
import protocolsupport.protocol.storage.ProtocolStorage;
import protocolsupport.utils.ReflectionUtils;

public class ConnectionImpl extends Connection {

	private static final Field channelwrapperField = getChannelWrapperField();
	private static Field getChannelWrapperField() {
		try {
			return ReflectionUtils.setAccessible(InitialHandler.class.getDeclaredField("ch"));
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException("Unable to get InitialHandler ChannelWrapper field", e);
		}
	}

	protected static final AttributeKey<ConnectionImpl> key = AttributeKey.valueOf("PSConnectionImpl");

	protected final CustomHandlerBoss boss;
	protected final InitialHandler initial;
	public ConnectionImpl(CustomHandlerBoss boss) {
		this.boss = boss;
		this.initial = (InitialHandler) boss.getHandler();
	}

	@Override
	public Object getNetworkManager() {
		return boss;
	}

	@Override
	public boolean isConnected() {
		return boss.isConnected();
	}

	@Override
	public InetSocketAddress getRawAddress() {
		return (InetSocketAddress) boss.getChannel().remoteAddress();
	}

	@Override
	public InetSocketAddress getAddress() {
		return initial.getAddress();
	}

	@Override
	public void changeAddress(InetSocketAddress newRemote) {
		try {
			ChannelWrapper channelwrapper = (ChannelWrapper) channelwrapperField.get(initial);
			ProtocolStorage.addAddress(getRawAddress(), newRemote);
			channelwrapper.setRemoteAddress(newRemote);
		} catch (Exception e) {
			throw new RuntimeException("Unable to change remote address", e);
		}
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

	@Override
	public String toString() {
		return MessageFormat.format(
			"{0}(player: {1}, address: {2}, rawaddress: {3}, version: {4}, metadata: {5})",
			getClass().getName(), getPlayer(), getAddress(), getRawAddress(), getVersion(), metadata
		);
	}

}
