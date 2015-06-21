package protocolsupport.protocol.transformer.v_1_5;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.util.internal.PlatformDependent;

import java.util.Objects;

import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.transformer.v_1_5.handlers.EntityRewriteUpstreamBridge;
import protocolsupport.protocol.transformer.v_1_5.handlers.ServerConnectHandler;
import protocolsupport.utils.ReflectionUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.Protocol;

public class LoginListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(LoginEvent event) {
		if (ProtocolSupportAPI.getProtocolVersion(event.getConnection().getAddress()) != ProtocolVersion.MINECRAFT_1_5_2) {
			return;
		}
		try {
			ReflectionUtils.setFieldValue(event, "done", new UserConnBootstrapInjectCallback((Callback<?>) ReflectionUtils.getFieldValue(event, "done")));
		} catch (Throwable t) {
			t.printStackTrace();
			event.setCancelled(true);
			event.setCancelReason("Failed to inject connection");
		}
	}

	private static class UserConnBootstrapInjectCallback implements Callback<LoginEvent> {

		private Callback<?> original;
		public UserConnBootstrapInjectCallback(Callback<?> original) {
			this.original = original;
		}

		@Override
		public void done(LoginEvent result, Throwable arg1) {
			try {
				final InitialHandler handler = ReflectionUtils.getFieldValue(original, "this$0");
				if (result.isCancelled()) {
					handler.disconnect(result.getCancelReason());
					return;
				}
				final ChannelWrapper ch = ReflectionUtils.getFieldValue(handler, "ch");
				if (ch.isClosed()) {
					return;
				}
				ch.getHandle().eventLoop().execute(new Runnable() {
					public void run() {
						if (ch.getHandle().isActive()) {
							BungeeCord bungee = BungeeCord.getInstance();

							ch.setProtocol(Protocol.GAME);

							UserConnection userCon = new UserConnection(bungee, ch, handler.getName(), handler);
							userCon.init();

							bungee.getPluginManager().callEvent(new PostLoginEvent(userCon));

							ch.getHandle().pipeline().get(HandlerBoss.class).setHandler(new EntityRewriteUpstreamBridge(bungee, userCon));
							ServerInfo server;
							if (bungee.getReconnectHandler() != null) {
								server = bungee.getReconnectHandler().getServer(userCon);
							} else {
								server = AbstractReconnectHandler.getForcedHost(handler);
							}
							if (server == null) {
								server = bungee.getServerInfo(handler.getListener().getDefaultServer());
							}
							connect(ch, bungee, userCon, server, true);

							try {
								ReflectionUtils.setFieldValue(handler, "thisState", Class.forName(InitialHandler.class.getName()+"$State").getEnumConstants()[5]);
							} catch (Throwable t) {
								t.printStackTrace();
								ch.close();
							}
						}
					}
				});
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	static void connect(final ChannelWrapper ch, final BungeeCord bungee, final UserConnection connection, final ServerInfo info, final boolean retry) {
		ServerConnectEvent event = new ServerConnectEvent(connection, info);
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
				ch.pipeline().addAfter("frame-decoder", "packet-decoder", new PacketDecoder(Protocol.HANDSHAKE, false, connection.getPendingConnection().getVersion()));
				ch.pipeline().remove("frame-decoder");
				ch.pipeline().addAfter("frame-prepender", "packet-encoder", new PacketEncoder(Protocol.HANDSHAKE, false, connection.getPendingConnection().getVersion()));
				ch.pipeline().remove("frame-prepender");
				((HandlerBoss) ch.pipeline().get(HandlerBoss.class)).setHandler(new ServerConnectHandler(bungee, connection, target));
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
						connect(ch, bungee, connection, info, false);
					} else if (connection.isDimensionChange()) {
						connection.disconnect(bungee.getTranslation("fallback_kick", new Object[] { future.cause().getClass().getName() }));
					} else {
						connection.sendMessage(bungee.getTranslation("fallback_kick", new Object[] { future.cause().getClass().getName() }));
					}
				}
			}
		};
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
