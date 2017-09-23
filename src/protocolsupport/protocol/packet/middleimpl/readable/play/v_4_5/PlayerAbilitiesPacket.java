package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5;

import protocolsupport.protocol.packet.middleimpl.readable.FixedLengthPassthroughReadableMiddlePacket;

public class PlayerAbilitiesPacket extends FixedLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0xCA;

	public PlayerAbilitiesPacket() {
		super(PACKET_ID, Byte.BYTES * 3);
	}

}
