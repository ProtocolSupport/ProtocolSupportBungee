package protocolsupport.protocol.packet.entityrewrite;

import protocolsupport.protocol.packet.id.LegacyPacketId;

public class LegacyFromClientEntityRewrite extends LegacyEntityRewrite {

	{
		register(
			LegacyPacketId.Serverbound.PLAY_ENTITY_ACTION,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Serverbound.PLAY_USE_ENTITY,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
		register(
			LegacyPacketId.Dualbound.PLAY_ANIMATION,
			EntityRewriteCommand.INT_ENTITY_ID_ENTITY_REWRITE_COMMAND,
			EntityRewriteCommand.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
		);
	}

}
