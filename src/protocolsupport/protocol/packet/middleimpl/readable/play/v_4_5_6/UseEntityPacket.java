package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.middleimpl.readable.FixedLengthPassthroughReadableMiddlePacket;

public class UseEntityPacket extends FixedLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0x07;

	public UseEntityPacket() {
		super(PACKET_ID, Integer.BYTES + Integer.BYTES + Byte.BYTES);
	}

}
