package protocolsupport.api.events;

import protocolsupport.api.Connection;

/**
 * This event is fired after player login start (after login start packet which contains client username)
 */
public class PlayerLoginStartEvent extends PlayerAbstractLoginEvent {

	protected final String hostname;
	protected boolean onlinemode;

	public PlayerLoginStartEvent(Connection connection, String hostname) {
		super(connection);
		this.hostname = hostname;
		this.onlinemode = connection.getProfile().isOnlineMode();
	}

	/**
	 * Returns hostname which player used when connecting to server
	 * @return hostname which player used when connecting to server
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * Returns true if online-mode checks will be used to auth player
	 * @return true if online-mode checks will be used to auth player
	 */
	public boolean isOnlineMode() {
		return onlinemode;
	}

	/**
	 * Sets if online-mode checks will be used to auth player
	 * @param onlinemode if online-mode checks will be used to auth player
	 */
	public void setOnlineMode(boolean onlinemode) {
		this.onlinemode = onlinemode;
	}

}
