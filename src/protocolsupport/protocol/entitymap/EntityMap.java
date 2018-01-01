package protocolsupport.protocol.entitymap;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.ProtocolType;
import protocolsupport.api.ProtocolVersion;

public abstract class EntityMap {

	public abstract void rewriteClientbound(ByteBuf buf, int oldId, int newId);

	public abstract void rewriteServerbound(ByteBuf buf, int oldId, int newId);

	private static final LegacyEntityMap legacyentitymap = new LegacyEntityMap();
	private static final PEEntityMap peentitymap = new PEEntityMap();

	public static EntityMap get(ProtocolVersion version) {
		if (version.getProtocolType() == ProtocolType.PC && version.isBefore(ProtocolVersion.MINECRAFT_1_7_5)) {
			return legacyentitymap;
		}
		if (version.getProtocolType() == ProtocolType.PE) {
			return peentitymap;
		}
		throw new IllegalArgumentException("Doesn't have entity map for version " + version);
	}

	protected static int rewriteInt(final ByteBuf packet, final int oldId, final int newId, final int offset) {
		final int readId = packet.getInt(offset);
		if (readId == oldId) {
			packet.setInt(offset, newId);
			return newId;
		} else if (readId == newId) {
			packet.setInt(offset, oldId);
			return oldId;
		}
		return -1;
	}

}
