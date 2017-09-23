package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.packet.middleimpl.readable.DynamicLengthPassthroughReadableMiddlePacket;
import protocolsupport.protocol.serializer.TypeCopier;

public class UpdateSignPacket extends DynamicLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0x82;

	public UpdateSignPacket() {
		super(PACKET_ID);
	}

	@Override
	protected void readTo(ByteBuf data, ByteBuf to) {
		TypeCopier.copyBytes(data, to, Integer.BYTES + Short.BYTES + Integer.BYTES);
		TypeCopier.copyString(data, to);
		TypeCopier.copyString(data, to);
		TypeCopier.copyString(data, to);
		TypeCopier.copyString(data, to);
	}

}
