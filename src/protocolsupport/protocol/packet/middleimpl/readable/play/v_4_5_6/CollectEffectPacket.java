package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.middleimpl.readable.FixedLengthPassthroughReadableMiddlePacket;

public class CollectEffectPacket extends FixedLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0x16;

	public CollectEffectPacket() {
		super(PACKET_ID, Integer.BYTES * 2);
	}

}
