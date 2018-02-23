package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDynamicLengthPassthroughReadableMiddlePacket;
import protocolsupport.protocol.serializer.TypeCopier;

public class UpdateSignPacket extends LegacyDynamicLengthPassthroughReadableMiddlePacket {

	public UpdateSignPacket() {
		super(LegacyPacketId.Dualbound.PLAY_UPDATE_SIGN);
	}

	@Override
	protected void readTo(ByteBuf data, ByteBuf to) {
		TypeCopier.copyBytes(data, to, Integer.BYTES + Short.BYTES + Integer.BYTES);
		TypeCopier.copyShortUTF16BEString(data, to);
		TypeCopier.copyShortUTF16BEString(data, to);
		TypeCopier.copyShortUTF16BEString(data, to);
		TypeCopier.copyShortUTF16BEString(data, to);
	}

}
