package protocolsupport.protocol.storage;

import java.util.UUID;

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

	private byte[] startGameData;

	public byte[] getStartGameData() {
		return startGameData;
	}

	public void setStartGameData(byte[] startGameData) {
		this.startGameData = startGameData;
	}

	private int realDimension = 0;

	public int getRealDimension() {
		return realDimension;
	}

	public void setRealDimension(int realDimension) {
		this.realDimension = realDimension;
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

	private boolean stashingClientPackets = false;

	public boolean isStashingClientPackets() {
		return stashingClientPackets;
	}

	public void setStashingClientPackets(boolean stashingClientPackets) {
		this.stashingClientPackets = stashingClientPackets;
	}

	private String skinData;

	public String getSkinData() {
		return skinData;
	}

	public void setSkinData(String skinData) {
		this.skinData = skinData;
	}

	private String skinGeometry;

	public String getSkinGeometry() {
		return skinGeometry;
	}

	public void setSkinGeometry(String skinGeometry) {
		this.skinGeometry = skinGeometry;
	}

}
