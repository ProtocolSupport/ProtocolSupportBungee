package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.middleimpl.readable.LegacyFixedLengthPassthroughReadableMiddlePacket;

public class PlayerPositionLookPacket extends LegacyFixedLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0x0D;

	public PlayerPositionLookPacket() {
		super(PACKET_ID, (Double.BYTES * 4) + (Float.BYTES * 2) + Byte.BYTES);
	}

}
