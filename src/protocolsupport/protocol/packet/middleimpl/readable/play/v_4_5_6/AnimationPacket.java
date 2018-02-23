package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyFixedLengthPassthroughReadableMiddlePacket;

public class AnimationPacket extends LegacyFixedLengthPassthroughReadableMiddlePacket {

	public AnimationPacket() {
		super(LegacyPacketId.Dualbound.PLAY_ANIMATION, Integer.BYTES + Byte.BYTES);
	}

}
