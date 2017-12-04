package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5;

import protocolsupport.protocol.packet.middleimpl.readable.LegacyFixedLengthPassthroughReadableMiddlePacket;

public class PlayerAbilitiesPacket extends LegacyFixedLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0xCA;

	public PlayerAbilitiesPacket() {
		super(PACKET_ID, Byte.BYTES * 3);
	}

}
