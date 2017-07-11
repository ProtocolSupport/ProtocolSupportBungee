package protocolsupport.api.events;

import net.md_5.bungee.api.plugin.Event;
import protocolsupport.api.Connection;

public abstract class ConnectionEvent extends Event {

	private final Connection connection;

	public ConnectionEvent(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		return this.connection;
	}

}
