package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.middleimpl.readable.FixedLengthPassthroughReadableMiddlePacket;

public class UseBedPacket extends FixedLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0x11;

	public UseBedPacket() {
		super(PACKET_ID, Integer.BYTES + Byte.BYTES + Integer.BYTES + Byte.BYTES + Integer.BYTES);
	}

}
