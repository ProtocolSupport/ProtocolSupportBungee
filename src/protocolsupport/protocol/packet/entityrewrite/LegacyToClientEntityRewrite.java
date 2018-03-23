package protocolsupport.protocol.packet.entityrewrite;

import java.util.function.IntUnaryOperator;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.serializer.TypeCopier;

public class LegacyToClientEntityRewrite extends LegacyEntityRewrite {

	{
		register(
			LegacyPacketId.Dualbound.PLAY_ANIMATION,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_USE_BED,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_SPAWN_NAMED_ENTITY,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_SPAWN_MOB,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_SPAWN_PAINTING,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_SPAWN_EXP_ORB,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_SPAWN_GLOBALENTITY,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_ENTITY,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_ENTITY_EQUIPMENT,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_ENTITY_VELOCITY,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_ENTITY_REL_MOVE,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_ENTITY_LOOK,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_ENTITY_REL_MOVE_LOOK,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_ENTITY_TELEPORT,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_ENTITY_HEAD_ROTATION,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_ENTITY_STATUS,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_ENTITY_METADATA,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_ENTITY_EFFECT_ADD,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_ENTITY_EFFECT_REMOVE,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_BLOCK_BREAK_ANIMATION,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_ENTITY_ATTACH,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_COLLECT_ENTITY,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Clientbound.PLAY_SPAWN_OBJECT,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			new EntityRewriteCommand() {
				@Override
				protected void rewrite(ByteBuf from, ByteBuf to, IntUnaryOperator rewritefunc) {
					int type = from.readUnsignedByte();
					to.writeByte(type);
					switch (type) {
						case 60:
						case 63:
						case 64:
						case 66:
						case 90: {
							TypeCopier.copyBytes(from, to, (Integer.BYTES * 3) + (Byte.BYTES * 2));
							int oldObjData = from.readInt();
							int newObjData = rewritefunc.applyAsInt(oldObjData);
							to.writeInt(newObjData);
							if (newObjData != 0) {
								if (oldObjData != 0) {
									to.writeBytes(from);
								} else {
									to.writeZero(Short.BYTES * 3);
								}
							}
							break;
						}
						default: {
							to.writeBytes(from);
							break;
						}
					}
				}
			}
		);
	}

}
