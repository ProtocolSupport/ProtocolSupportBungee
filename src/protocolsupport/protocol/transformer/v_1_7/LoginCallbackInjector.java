package protocolsupport.protocol.transformer.v_1_7;

import protocolsupport.protocol.listeners.LoginFinishInjector.ILoginCallbackInjector;
import protocolsupport.utils.ReflConstants;
import protocolsupport.utils.ReflectionUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.UpstreamBridge;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.packet.LoginSuccess;

public class LoginCallbackInjector implements ILoginCallbackInjector {

	@Override
	public void inject(LoginEvent event) {
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

				final ChannelWrapper ch = ReflectionUtils.getFieldValue(handler, "ch");

				if (result.isCancelled()) {
					handler.disconnect(result.getCancelReason());
					return;
				}

				if (ch.isClosed()) {
					return;
				}

				ch.getHandle().eventLoop().execute(new Runnable() {
					public void run() {
						if (ch.getHandle().isActive()) {
							BungeeCord bungee = BungeeCord.getInstance();

							if (handler.getVersion() >= 5) {
								handler.unsafe().sendPacket(new LoginSuccess(handler.getUniqueId().toString(), handler.getName()));
							} else {
								handler.unsafe().sendPacket(new LoginSuccess(handler.getUUID(), handler.getName()));
							}

							ch.setProtocol(Protocol.GAME);

							UserConnection userCon = new UserConnection(bungee, ch, handler.getName(), handler);
							userCon.init();

							bungee.getPluginManager().callEvent(new PostLoginEvent(userCon));

							ch.getHandle().pipeline().get(HandlerBoss.class).setHandler(new UpstreamBridge(bungee, userCon));
							ServerInfo server;
							if (bungee.getReconnectHandler() != null) {
								server = bungee.getReconnectHandler().getServer(userCon);
							} else {
								server = AbstractReconnectHandler.getForcedHost(handler);
							}
							if (server == null) {
								server = bungee.getServerInfo(handler.getListener().getDefaultServer());
							}

							ServerConnector.connect(bungee, userCon, server, true);

							try {
								ReflectionUtils.setFieldValue(handler, "thisState", ReflConstants.getInitialHandlerFinishedState());
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



}
