package protocolsupport.protocol.pipeline.version.v_pe;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import protocolsupport.api.Connection;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.serializer.PEPacketIdSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.storage.NetworkDataCache;
import protocolsupport.utils.netty.Allocator;

//TODO: fully implement
public class ToClientEntityRewriteHandler extends MessageToMessageEncoder<ByteBuf> {

	protected final Connection connection;
	protected final NetworkDataCache cache;

	public ToClientEntityRewriteHandler(Connection connection, NetworkDataCache cache) {
		this.connection = connection;
		this.cache = cache;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
		buf.markReaderIndex();
		int packetId = PEPacketIdSerializer.readPacketId(buf);
		switch (packetId) {
			case PEPacketId.Dualbound.PLAY_PLAYER_MOVE_LOOK: {
				int entityId = (int) VarNumberSerializer.readVarLong(buf);
				entityId = cache.replaceEntityId(entityId);
				ByteBuf packet = Allocator.allocateBuffer();
				PEPacketIdSerializer.writePacketId(packet, packetId);
				VarNumberSerializer.writeVarLong(packet, entityId);
				packet.writeBytes(buf);
				out.add(packet);
			}
			default: {
				buf.resetReaderIndex();
				out.add(buf.retain());
			}
		}
	}

}
