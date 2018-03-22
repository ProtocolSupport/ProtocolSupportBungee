package protocolsupport.protocol.packet.entityrewrite;

import java.util.EnumMap;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;

import protocolsupport.api.ProtocolVersion;

public class EntityRewriteFactory {

	protected static EnumMap<ProtocolVersion, EntityRewrite> toClient = new EnumMap<>(ProtocolVersion.class);
	protected static EnumMap<ProtocolVersion, EntityRewrite> fromClient = new EnumMap<>(ProtocolVersion.class);
	static {
		toClient.put(ProtocolVersion.MINECRAFT_PE, new PEToClientEntityRewrite());
		fromClient.put(ProtocolVersion.MINECRAFT_PE, new PEFromClientEntityRewrite());
	}

	public static EntityRewrite getToClientRewrite(ProtocolVersion version) {
		return toClient.get(version);
	}

	public static EntityRewrite getFromClientRewrite(ProtocolVersion version) {
		return fromClient.get(version);
	}

	public static IntUnaryOperator createReplaceEntityIdFunc(IntSupplier id1Supplier, IntSupplier id2Supplier) {
		return (entityId) -> {
			int entityId1 = id1Supplier.getAsInt();
			int entityId2 = id2Supplier.getAsInt();
			if (entityId == entityId1) {
				return entityId2;
			}
			if (entityId == entityId2) {
				return entityId1;
			}
			return entityId;
		};
	}

}