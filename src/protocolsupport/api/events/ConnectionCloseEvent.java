package protocolsupport.api.events;

import protocolsupport.api.Connection;

public class ConnectionCloseEvent extends ConnectionEvent {

	public ConnectionCloseEvent(Connection connection) {
		super(connection);
	}

}
