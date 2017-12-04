package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.middleimpl.readable.LegacyFixedLengthPassthroughReadableMiddlePacket;

public class AnimationPacket extends LegacyFixedLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0x12;

	public AnimationPacket() {
		super(PACKET_ID, Integer.BYTES + Byte.BYTES);
	}

}
