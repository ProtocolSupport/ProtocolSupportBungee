package protocolsupport.api;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public abstract class Connection {

	protected volatile ProtocolVersion version = ProtocolVersion.UNKNOWN;

	/**
	 * Returns native network manager object
	 * This can be anything, for bungeecord its HandlerBoss
	 * @return native network manager object
	 */
	public abstract Object getNetworkManager();

	/**
	 * Returns if this connection is active
	 * @return true if connection is active
	 */
	public abstract boolean isConnected();

	/**
	 * Returns real remote address
	 * @return real remote address
	 */
	public abstract InetSocketAddress getRawAddress();

	/**
	 * Returns remote address
	 * @return remote address
	 */
	public abstract InetSocketAddress getAddress();

	/**
	 * Returns {@link ProxiedPlayer} object if possible
	 * @return {@link ProxiedPlayer} object or null
	 */
	public abstract ProxiedPlayer getPlayer();

	/**
	 * Returns {@link ProtocolVersion}
	 * Returns UNKNOWN if handshake packet is not yet received
	 * @return {@link ProtocolVersion}
	 */
	public ProtocolVersion getVersion() {
		return version;
	}

	protected final ConcurrentHashMap<String, Object> metadata = new ConcurrentHashMap<>();

	/**
	 * Adds any object to the internal map
	 * @param key map key
	 * @param obj value
	 */
	public void addMetadata(String key, Object obj) {
		metadata.put(key, obj);
	}

	/**
	 * Returns object from internal map by map key
	 * Returns null if there wasn't any object by map key
	 * @param key map key
	 * @return value from internal map
	 */
	public Object getMetadata(String key) {
		return metadata.get(key);
	}

	/**
	 * Removes object from internal map by map key
	 * Returns null if there wasn't any object by map key
	 * @param key map key
	 * @return deleted value from internal map
	 */
	public Object removeMetadata(String key) {
		return metadata.remove(key);
	}

	/**
	 * Returns if there is a value in internal map by map key
	 * @param key map key
	 * @return true is there was any object by map key
	 */
	public boolean hasMetadata(String key) {
		return metadata.containsKey(key);
	}

}
