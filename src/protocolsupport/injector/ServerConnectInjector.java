package protocolsupport.injector;

import java.util.HashMap;

import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ServerConnectInjector implements Listener {

	private HashMap<ProtocolVersion, IServerConnector> connectors = new HashMap<ProtocolVersion, IServerConnector>();
	{
		protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.handlers.ServerConnector legacyConnector = new protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.handlers.ServerConnector();
		connectors.put(ProtocolVersion.MINECRAFT_1_6_4, legacyConnector);
		connectors.put(ProtocolVersion.MINECRAFT_1_6_2, legacyConnector);
		connectors.put(ProtocolVersion.MINECRAFT_1_5_2, legacyConnector);
		connectors.put(ProtocolVersion.MINECRAFT_1_4_7, legacyConnector);
	};

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onServerConnect(ServerConnectEvent event) {
		IServerConnector connector = connectors.get(ProtocolSupportAPI.getProtocolVersion(event.getPlayer().getAddress()));
		if (connector == null) {
			return;
		}
		event.setCancelled(true);
		connector.connect((UserConnection) event.getPlayer(), event.getTarget());
	}

	public static interface IServerConnector {

		public void connect(UserConnection connection, ServerInfo target);

	}

}
