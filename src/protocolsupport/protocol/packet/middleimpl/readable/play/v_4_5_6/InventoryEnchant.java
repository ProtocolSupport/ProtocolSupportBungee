package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.middleimpl.readable.FixedLengthPassthroughReadableMiddlePacket;

public class InventoryEnchant extends FixedLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0x6C;

	public InventoryEnchant() {
		super(0x6C, Byte.BYTES * 2);
	}

}
