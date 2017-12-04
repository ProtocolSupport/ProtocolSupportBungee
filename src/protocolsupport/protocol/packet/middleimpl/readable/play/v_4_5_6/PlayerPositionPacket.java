package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.middleimpl.readable.LegacyFixedLengthPassthroughReadableMiddlePacket;

public class PlayerPositionPacket extends LegacyFixedLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0x0B;

	public PlayerPositionPacket() {
		super(PACKET_ID, (Double.BYTES * 4) + Byte.BYTES);
	}

}
