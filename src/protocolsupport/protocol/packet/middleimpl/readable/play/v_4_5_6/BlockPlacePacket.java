package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDynamicLengthPassthroughReadableMiddlePacket;
import protocolsupport.protocol.serializer.TypeCopier;

public class BlockPlacePacket extends LegacyDynamicLengthPassthroughReadableMiddlePacket {

	public BlockPlacePacket() {
		super(LegacyPacketId.Serverbound.PLAY_BLOCK_PLACE);
	}

	@Override
	protected void readTo(ByteBuf data, ByteBuf to) {
		TypeCopier.copyBytes(data, to, Integer.BYTES + Byte.BYTES + Integer.BYTES + Byte.BYTES);
		TypeCopier.copyLegacyItemStack(data, to);
		TypeCopier.copyBytes(data, to, Byte.BYTES * 3);
	}

}
