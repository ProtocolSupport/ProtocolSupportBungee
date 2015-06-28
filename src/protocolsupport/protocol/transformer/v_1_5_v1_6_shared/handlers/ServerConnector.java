package protocolsupport.protocol.transformer.v_1_5_v1_6_shared.handlers;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.util.internal.PlatformDependent;

import java.util.Objects;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.Protocol;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.protocol.listeners.ServerConnectListener.IServerConnector;
import protocolsupport.protocol.listeners.ServerConnectListener.ProtocolSupoortBungeeServerConnectedEvent;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.PacketDecoder;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.PacketEncoder;
import protocolsupport.utils.ReflectionUtils;

public class ServerConnector implements IServerConnector {

	@Override
	public void connect(UserConnection connection, ServerInfo target) {
		connect(BungeeCord.getInstance(), connection, target, false);
	}

	public static void connect(final BungeeCord bungee, final UserConnection connection, final ServerInfo info, final boolean retry) {
		ServerConnectEvent event = new ProtocolSupoortBungeeServerConnectedEvent(connection, info);
		if (bungee.getPluginManager().callEvent(event).isCancelled()) {
			return;
		}

		final BungeeServerInfo target = (BungeeServerInfo) event.getTarget();
		final ServerConnection serverconn = connection.getServer();
		if ((serverconn != null) && (Objects.equals(serverconn.getInfo(), target))) {
			connection.sendMessage(bungee.getTranslation("already_connected", new Object[0]));
			return;
		}
		if (connection.getPendingConnects().contains(target)) {
			connection.sendMessage(bungee.getTranslation("already_connecting", new Object[0]));
			return;
		}
		connection.getPendingConnects().add(target);

		ChannelInitializer<Channel> initializer = new ChannelInitializer<Channel>() {
			protected void initChannel(Channel ch) throws Exception {
				PipelineUtils.BASE.initChannel(ch);
				ch.pipeline().addAfter(PipelineUtils.FRAME_DECODER, PipelineUtils.PACKET_DECODER, new PacketDecoder(false, ProtocolSupportAPI.getProtocolVersion(connection.getAddress())));
				ch.pipeline().addAfter(PipelineUtils.FRAME_PREPENDER, PipelineUtils.PACKET_ENCODER, new PacketEncoder(Protocol.HANDSHAKE, false, connection.getPendingConnection().getVersion()));
				ch.pipeline().remove(PipelineUtils.FRAME_DECODER);
				ch.pipeline().remove(PipelineUtils.FRAME_PREPENDER);
				ch.pipeline().get(HandlerBoss.class).setHandler(new ServerConnectHandler(bungee, connection, target));
			}
		};

		ChannelFutureListener listener = new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future) throws Exception {
				if (!future.isSuccess()) {
					future.channel().close();
					connection.getPendingConnects().remove(target);

					ServerInfo def = (ServerInfo) bungee.getServers().get(connection.getPendingConnection().getListener().getFallbackServer());
					if ((retry) && (target != def) && ((serverconn == null) || (def != serverconn.getInfo()))) {
						connection.sendMessage(bungee.getTranslation("fallback_lobby", new Object[0]));
						connect(bungee, connection, info, false);
					} else if (connection.isDimensionChange()) {
						connection.disconnect(bungee.getTranslation("fallback_kick", new Object[] { future.cause().getClass().getName() }));
					} else {
						connection.sendMessage(bungee.getTranslation("fallback_kick", new Object[] { future.cause().getClass().getName() }));
					}
				}
			}
		};

		ChannelWrapper ch = null;
		try {
			ch = ReflectionUtils.getFieldValue(connection, "ch");
		} catch (Throwable t) {
			t.printStackTrace();
			connection.disconnect("Failed to get ChannelWrapper from UserConnection");
			return;
		}

		Bootstrap b = new Bootstrap()
		.channel(PipelineUtils.getChannel())
		.group(ch.getHandle().eventLoop())
		.handler(initializer)
		.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Integer.valueOf(5000))
		.remoteAddress(target.getAddress());
		if ((connection.getPendingConnection().getListener().isSetLocalAddress()) && (!PlatformDependent.isWindows())) {
			b.localAddress(connection.getPendingConnection().getListener().getHost().getHostString(), 0);
		}
		b.connect().addListener(listener);
	}

}
