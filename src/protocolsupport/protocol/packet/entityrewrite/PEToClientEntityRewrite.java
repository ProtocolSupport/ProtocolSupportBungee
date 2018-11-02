package protocolsupport.protocol.packet.entityrewrite;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;

public class PEToClientEntityRewrite extends PEEntityRewrite {

	{
		register(
			PEPacketId.Clientbound.PLAY_ENTITY_ATTRIBUTES,
			EntityRewriteCommand.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			PEPacketId.Clientbound.PLAY_ENTITY_EFFECT,
			EntityRewriteCommand.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			PEPacketId.Clientbound.PLAY_ENTITY_TELEPORT,
			EntityRewriteCommand.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			PEPacketId.Clientbound.PLAY_ENTITY_VELOCITY,
			EntityRewriteCommand.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			PEPacketId.Clientbound.PLAY_ENTITY_STATUS,
			EntityRewriteCommand.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			PEPacketId.Clientbound.PLAY_ENTITY_DESTROY,
			EntityRewriteCommand.SVARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND
		);
		register(
			PEPacketId.Clientbound.PLAY_ENTITY_COLLECT_EFFECT,
			EntityRewriteCommand.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND
		);
		register(
			PEPacketId.Clientbound.PLAY_ENTITY_PASSENGER,
			EntityRewriteCommand.SVARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.SVARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			PEPacketId.Clientbound.PLAY_ENTITY_ANIMATION,
			new EntityRewriteCommand.NoEntityIdRewriteEntityRewriteCommand() {
				@Override
				protected void rewrite(ByteBuf from, ByteBuf to) {
					VarNumberSerializer.writeSVarInt(to, VarNumberSerializer.readSVarInt(from));
				}
			},
			EntityRewriteCommand.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND
		);
		register(
			PEPacketId.Dualbound.PLAY_PLAYER_MOVE_LOOK,
			EntityRewriteCommand.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			new EntityRewriteCommand.FixedLengthBytesCopyEntityRewriteCommand((Float.BYTES * 6) + (Byte.BYTES * 2)),
			EntityRewriteCommand.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND
		);
		//TODO: meta content remap
		register(
			PEPacketId.Clientbound.PLAY_ENTITY_METADATA,
			EntityRewriteCommand.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		//TODO: meta content remap
		register(
			PEPacketId.Clientbound.PLAY_ENTITY_SPAWN,
			EntityRewriteCommand.SVARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		//TODO: meta content remap
		register(
			PEPacketId.Clientbound.PLAY_SPAWN_PLAYER,
			new EntityRewriteCommand.FixedLengthBytesCopyEntityRewriteCommand(Long.BYTES * 2),
			new EntityRewriteCommand.NoEntityIdRewriteEntityRewriteCommand() {
				@Override
				protected void rewrite(ByteBuf from, ByteBuf to) {
					StringSerializer.writeVarIntUTF8String(to, StringSerializer.readVarIntUTF8String(from)); // username
				}
			},
			EntityRewriteCommand.SVARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
	}
}
