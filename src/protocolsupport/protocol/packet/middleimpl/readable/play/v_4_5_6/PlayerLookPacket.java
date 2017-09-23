package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.middleimpl.readable.FixedLengthPassthroughReadableMiddlePacket;

public class PlayerLookPacket extends FixedLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0x0C;

	public PlayerLookPacket() {
		super(PACKET_ID, (Float.BYTES * 2) + Byte.BYTES);
	}

}
