package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.middleimpl.readable.FixedLengthPassthroughReadableMiddlePacket;

public class PlayerFlyingPacket extends FixedLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0x0A;

	public PlayerFlyingPacket() {
		super(PACKET_ID, Byte.BYTES);
	}

}
