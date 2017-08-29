package protocolsupport.protocol.packet.middleimpl.readable.play.v_6;

import protocolsupport.protocol.packet.middleimpl.readable.FixedLengthPassthroughReadableMiddlePacket;

public class PlayerAbilitiesPacket extends FixedLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0xCA;

	public PlayerAbilitiesPacket() {
		super(PACKET_ID, Byte.BYTES + (Float.BYTES * 2));
	}

}
