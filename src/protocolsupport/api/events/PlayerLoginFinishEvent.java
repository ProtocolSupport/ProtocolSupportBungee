package protocolsupport.api.events;

import java.util.UUID;

import protocolsupport.api.Connection;

/**
 * This event is fired when player login finishes (after online-mode processing and uuid generation, but before actual world join)
 * This event is fired only if {@link PlayerLoginStartEvent} has fired for this client
 */
public class PlayerLoginFinishEvent extends PlayerAbstractLoginEvent {

	private final UUID uuid;
	private final boolean onlineMode;

	public PlayerLoginFinishEvent(Connection connection, String username, UUID uuid, boolean onlineMode) {
		super(connection, username);
		this.uuid = uuid;
		this.onlineMode = onlineMode;
	}

	/**
	 * Returns player uuid
	 * @return player uuid
	 */
	public UUID getUUID() {
		return uuid;
	}

	/**
	 * Returns true if this player logged in using online-mode checks
	 * @return true if this player logged in using online-mode checks
	 */
	public boolean isOnlineMode() {
		return onlineMode;
	}

}
