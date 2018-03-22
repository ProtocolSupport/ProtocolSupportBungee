package protocolsupport.api;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
	 * Changes remote address <br>
	 * This address will be available as parameter for ProtocolSupportAPI until connection close
	 * @param newRemote new remote address
	 */
	public abstract void changeAddress(InetSocketAddress newRemote);

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

	/**
	 * Sends packet to client <br>
	 * Packet sent by this method skips send packet listener
	 * @param packet packet
	 */
	public abstract void sendPacketToClient(Object packet);

	/**
	 * Sends packet to server <br>
	 * Packet sent by this method skips send packet listener
	 * @param packet packet
	 */
	public abstract void sendPacketToServer(Object packet);

	protected final CopyOnWriteArrayList<PacketListener> clientConnectionPacketListeners = new CopyOnWriteArrayList<>();

	/**
	 * Adds packet listener to server connection
	 * @param listener packet listener
	 */
	public void addClientConnectionPacketListener(PacketListener listener) {
		clientConnectionPacketListeners.add(listener);
	}

	/**
	 * Removes packet listener from server connection
	 * @param listener packet listener
	 */
	public void removeClientConnectionPacketListener(PacketListener listener) {
		clientConnectionPacketListeners.remove(listener);
	}

	protected final CopyOnWriteArrayList<PacketListener> serverConnectionPacketListeners = new CopyOnWriteArrayList<>();

	/**
	 * Adds packet listener to server connection
	 * @param listener packet listener
	 */
	public void addServerConnectionPacketListener(PacketListener listener) {
		serverConnectionPacketListeners.add(listener);
	}

	/**
	 * Removes packet listener from server connection
	 * @param listener packet listener
	 */
	public void removeServerConnectionPacketListener(PacketListener listener) {
		serverConnectionPacketListeners.remove(listener);
	}

	public abstract static class PacketListener {

		/**
		 * Override to handle native packet sending <br>
		 * PacketEvent and it's data is only valid while handling the packet
		 * @param event packet event
		 */
		public void onPacketSending(PacketEvent event) {
		}

		/**
		 * Override to handle native packet receiving <br>
		 * PacketEvent and it's data is only valid while handling the packet <br>
		 * Based on client version this the received data might be a part of packet, not a full one
		 * @param event packet event
		 */
		public void onPacketReceiving(PacketEvent event) {
		}

		public static class PacketEvent {

			protected Object packet;
			protected boolean cancelled;

			/**
			 * Returns packet
			 * @return native packet instance
			 */
			public Object getPacket() {
				return packet;
			}

			/**
			 * Sets packet
			 * @param packet native packet instance
			 */
			public void setPacket(Object packet) {
				this.packet = packet;
			}

			/**
			 * Returns if packet is cancelled
			 * @return true if packet is cancelled, false otherwise
			 */
			public boolean isCancelled() {
				return cancelled;
			}

			/**
			 * Sets if packet is cancelled
			 * @param cancelled true if packet is cancelled, false otherwise
			 */
			public void setCancelled(boolean cancelled) {
				this.cancelled = cancelled;
			}
		}

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
