package protocolsupport.protocol.utils;

import java.util.Arrays;

import gnu.trove.map.hash.TIntObjectHashMap;
import protocolsupport.api.ProtocolType;
import protocolsupport.api.ProtocolVersion;

public class ProtocolVersionsHelper {

	private static final TIntObjectHashMap<ProtocolVersion> byOldProtocolId = new TIntObjectHashMap<>();
	private static final TIntObjectHashMap<ProtocolVersion> byNewProtocolId = new TIntObjectHashMap<>();
	private static final TIntObjectHashMap<ProtocolVersion> byPEProtocolId = new TIntObjectHashMap<>();

	public static ProtocolVersion getOldProtocolVersion(int protocolid) {
		ProtocolVersion version = byOldProtocolId.get(protocolid);
		return version != null ? version : ProtocolVersion.MINECRAFT_LEGACY;
	}

	public static ProtocolVersion getNewProtocolVersion(int protocolid) {
		ProtocolVersion version = byNewProtocolId.get(protocolid);
		return version != null ? version : ProtocolVersion.MINECRAFT_FUTURE;
	}

	public static ProtocolVersion getPEProtocolVersion(int protocolid) {
		ProtocolVersion version = byPEProtocolId.get(protocolid);
		return version != null ? version : ProtocolVersion.MINECRAFT_PE_FUTURE;
	}

	public static final ProtocolVersion LATEST_PC = ProtocolVersion.getLatest(ProtocolType.PC);
	public static final ProtocolVersion LATEST_PE = ProtocolVersion.getLatest(ProtocolType.PE);

	public static final ProtocolVersion[] ALL_PE = ProtocolVersion.getAllBetween(ProtocolVersion.getOldest(ProtocolType.PE), LATEST_PE);

	static {
		Arrays.stream(ProtocolVersion.getAllBeforeI(ProtocolVersion.MINECRAFT_1_6_4)).forEach(version -> byOldProtocolId.put(version.getId(), version));
		Arrays.stream(ProtocolVersion.getAllAfterI(ProtocolVersion.MINECRAFT_1_7_5)).forEach(version -> byNewProtocolId.put(version.getId(), version));
		Arrays.stream(ALL_PE).forEach(version -> byPEProtocolId.put(version.getId(), version));
	}

}
