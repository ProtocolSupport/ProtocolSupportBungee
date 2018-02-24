package protocolsupport.protocol.pipeline.version.v_pe;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import protocolsupport.api.Connection;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.serializer.PEPacketIdSerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.storage.NetworkDataCache;
import protocolsupport.protocol.utils.EntityRewriteTools;

//TODO: fully implement
public class ToClientEntityRewriteHandler extends MessageToMessageEncoder<ByteBuf> {

	protected static final EntityRewriteTools rewrite = new EntityRewriteTools(256) {
		{
			register(
				PEPacketId.Dualbound.PLAY_PLAYER_MOVE_LOOK,
				EntityRewriteTools.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
				EntityRewriteTools.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
			);
			register(
				PEPacketId.Clientbound.PLAY_ENTITY_ATTRIBUTES,
				EntityRewriteTools.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
				EntityRewriteTools.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
			);
			register(
				PEPacketId.Clientbound.PLAY_ENTITY_EFFECT,
				EntityRewriteTools.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
				EntityRewriteTools.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
			);
			register(
				PEPacketId.Clientbound.PLAY_ENTITY_TELEPORT,
				EntityRewriteTools.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
				EntityRewriteTools.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
			);
			register(
				PEPacketId.Clientbound.PLAY_ENTITY_VELOCITY,
				EntityRewriteTools.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
				EntityRewriteTools.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
			);
			register(
				PEPacketId.Clientbound.PLAY_ENTITY_STATUS,
				EntityRewriteTools.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
				EntityRewriteTools.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
			);
			register(
				PEPacketId.Clientbound.PLAY_ENTITY_DESTROY,
				EntityRewriteTools.SVARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND
			);
			register(
				PEPacketId.Clientbound.PLAY_ENTITY_COLLECT_EFFECT,
				EntityRewriteTools.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
				EntityRewriteTools.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND
			);
			register(
				PEPacketId.Clientbound.PLAY_ENTITY_PASSENGER,
				EntityRewriteTools.SVARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
				EntityRewriteTools.SVARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND
			);
			register(
				PEPacketId.Clientbound.PLAY_ENTITY_ANIMATION,
				new NoEntityIdRewriteEntityRewriteCommand() {
					@Override
					protected void rewrite(ByteBuf from, ByteBuf to) {
						VarNumberSerializer.writeSVarLong(to, VarNumberSerializer.readSVarLong(from));
					}
				},
				EntityRewriteTools.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND
			);
			//TODO: meta content remap
			register(
				PEPacketId.Clientbound.PLAY_ENTITY_METADATA,
				EntityRewriteTools.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
				EntityRewriteTools.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
			);
			//TODO: meta content remap
			register(
				PEPacketId.Clientbound.PLAY_ENTITY_SPAWN,
				EntityRewriteTools.SVARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
				EntityRewriteTools.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
				EntityRewriteTools.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
			);
			//TODO: meta content remap
			register(
				PEPacketId.Clientbound.PLAY_SPAWN_PLAYER,
				new FixedLengthBytesCopyEntityRewriteCommand(Long.BYTES * 2),
				new NoEntityIdRewriteEntityRewriteCommand() {
					@Override
					protected void rewrite(ByteBuf from, ByteBuf to) {
						StringSerializer.writeVarIntUTF8String(to, StringSerializer.readVarIntUTF8String(from));
					}
				},
				EntityRewriteTools.SVARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
				EntityRewriteTools.VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND,
				EntityRewriteTools.REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND
			);
		}
		@Override
		protected int readPacketId(ByteBuf from) {
			return PEPacketIdSerializer.readPacketId(from);
		}
		@Override
		protected void writePacketId(ByteBuf to, int packetId) {
			PEPacketIdSerializer.writePacketId(to, packetId);
		}
	};

	protected final Connection connection;
	protected final NetworkDataCache cache;

	public ToClientEntityRewriteHandler(Connection connection, NetworkDataCache cache) {
		this.connection = connection;
		this.cache = cache;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
		out.add(rewrite.rewrite(buf, cache::replaceEntityId));
	}

}
