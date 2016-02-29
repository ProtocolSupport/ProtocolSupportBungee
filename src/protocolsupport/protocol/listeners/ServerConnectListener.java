package protocolsupport.protocol.listeners;

import java.util.HashMap;

import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ServerConnectListener implements Listener {

	@SuppressWarnings("serial")
	private HashMap<ProtocolVersion, IServerConnector> connectors = new HashMap<ProtocolVersion, IServerConnector>() {{
		put(ProtocolVersion.MINECRAFT_1_9, new protocolsupport.protocol.transformer.v_1_7.ServerConnector());
		put(ProtocolVersion.MINECRAFT_1_7_10, new protocolsupport.protocol.transformer.v_1_7.ServerConnector());
		put(ProtocolVersion.MINECRAFT_1_7_5, new protocolsupport.protocol.transformer.v_1_7.ServerConnector());
		put(ProtocolVersion.MINECRAFT_1_6_4, new protocolsupport.protocol.transformer.v_1_5_v1_6_shared.handlers.ServerConnector());
		put(ProtocolVersion.MINECRAFT_1_6_2, new protocolsupport.protocol.transformer.v_1_5_v1_6_shared.handlers.ServerConnector());
		put(ProtocolVersion.MINECRAFT_1_5_2, new protocolsupport.protocol.transformer.v_1_5_v1_6_shared.handlers.ServerConnector());
	}};

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onServerConnect(ServerConnectEvent event) {
		IServerConnector connector = connectors.get(ProtocolSupportAPI.getProtocolVersion(event.getPlayer().getAddress()));
		if (connector == null) {
			return;
		}
		if (event.isCancelled() || event instanceof ProtocolSupoortBungeeServerConnectedEvent) {
			return;
		}
		event.setCancelled(true);
		connector.connect((UserConnection) event.getPlayer(), event.getTarget());
	}

	public static interface IServerConnector {

		public void connect(UserConnection connection, ServerInfo target);

	}

	public static class ProtocolSupoortBungeeServerConnectedEvent extends ServerConnectEvent {

		public ProtocolSupoortBungeeServerConnectedEvent(ProxiedPlayer player, ServerInfo target) {
			super(player, target);
		}
		
	}

}
