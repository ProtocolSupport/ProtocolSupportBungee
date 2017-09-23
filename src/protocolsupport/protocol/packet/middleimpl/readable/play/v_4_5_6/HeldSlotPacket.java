package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.middleimpl.readable.FixedLengthPassthroughReadableMiddlePacket;

public class HeldSlotPacket extends FixedLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0x10;

	public HeldSlotPacket() {
		super(PACKET_ID, Short.BYTES);
	}

}
