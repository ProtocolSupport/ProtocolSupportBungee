package protocolsupport.api.events;

import protocolsupport.api.Connection;

public class ConnectionOpenEvent extends ConnectionEvent {

	public ConnectionOpenEvent(Connection connection) {
		super(connection);
	}

}
