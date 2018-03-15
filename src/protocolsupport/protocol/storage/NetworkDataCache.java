package protocolsupport.protocol.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import org.apache.commons.lang3.Validate;

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

	private Handshake serverHandshake;

	public void setServerHandshake(Handshake serverHandshake) {
		this.serverHandshake = serverHandshake;
	}

	public Handshake getServerHandshake() {
		return serverHandshake;
	}

	protected String locale = "en_us";

	public void setLocale(String locale) {
		Validate.notNull(locale, "Client locale can't be null");
		this.locale = locale.toLowerCase();
	}

	public String getLocale() {
		return locale;
	}

	private UUID peClientUUID;

	public void setPEClientUUID(UUID uuid) {
		Validate.notNull(uuid, "PE client uuid (identity) can't be null");
		this.peClientUUID = uuid;
	}

	public UUID getPEClientUUID() {
		return peClientUUID;
	}

	private int clientEntityId;
	private int serverEntityId;

	public void setClientEntityId(int entityId) {
		this.clientEntityId = entityId;
	}

	public void setServerEntityId(int entityId) {
		this.serverEntityId = entityId;
	}

	public int replaceEntityId(int entityId) {
		if (entityId == clientEntityId) {
			return serverEntityId;
		}
		if (entityId == serverEntityId) {
			return clientEntityId;
		}
		return entityId;
	}

	private Map<PlayerListItem, ByteBuf> tabList = new HashMap<>();

	public Map<PlayerListItem, ByteBuf> tabList() {
		return tabList;
	}
}
