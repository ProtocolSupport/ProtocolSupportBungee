package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.packet.middleimpl.readable.DynamicLengthPassthroughReadableMiddlePacket;
import protocolsupport.protocol.serializer.TypeCopier;

public class BlockPlacePacket extends DynamicLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0x0F;

	public BlockPlacePacket() {
		super(PACKET_ID);
	}

	@Override
	protected void readTo(ByteBuf data, ByteBuf to) {
		TypeCopier.copyBytes(data, to, Integer.BYTES + Byte.BYTES + Integer.BYTES + Byte.BYTES);
		TypeCopier.copyItemStack(data, to);
		TypeCopier.copyBytes(data, to, Byte.BYTES * 3);
	}

}
