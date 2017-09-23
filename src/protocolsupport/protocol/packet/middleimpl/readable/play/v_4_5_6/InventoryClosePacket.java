package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.middleimpl.readable.FixedLengthPassthroughReadableMiddlePacket;

public class InventoryClosePacket extends FixedLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0x65;

	public InventoryClosePacket() {
		super(PACKET_ID, Byte.BYTES);
	}

}
