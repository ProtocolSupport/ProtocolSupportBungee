package protocolsupport.protocol.storage;

import java.util.UUID;

import net.md_5.bungee.protocol.packet.Handshake;
import protocolsupport.api.Connection;

public class NetworkDataCache {

	private static final String METADATA_KEY = "__PSB_NDC";

	public static NetworkDataCache getFrom(Connection connection) {
		return (NetworkDataCache) connection.getMetadata(METADATA_KEY);
	}

	public void storeIn(Connection connection) {
		connection.addMetadata(METADATA_KEY, this);
	}

	public Handshake serverHandshake;

	public UUID peClientUUID;

}
