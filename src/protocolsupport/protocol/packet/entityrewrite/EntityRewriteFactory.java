package protocolsupport.protocol.packet.entityrewrite;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;

import protocolsupport.api.ProtocolVersion;

public class EntityRewriteFactory {

	protected static EnumMap<ProtocolVersion, EntityRewrite> toClient = new EnumMap<>(ProtocolVersion.class);
	protected static EnumMap<ProtocolVersion, EntityRewrite> fromClient = new EnumMap<>(ProtocolVersion.class);
	static {
		Arrays.stream(ProtocolVersion.getAllBetween(ProtocolVersion.MINECRAFT_1_4_7, ProtocolVersion.MINECRAFT_1_6_4))
		.forEach(version -> {
			toClient.put(version, new LegacyToClientEntityRewrite());
			fromClient.put(version, new LegacyFromClientEntityRewrite());
		});
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
