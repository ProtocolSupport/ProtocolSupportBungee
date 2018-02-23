package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyFixedLengthPassthroughReadableMiddlePacket;

public class InventorySelectEnchant extends LegacyFixedLengthPassthroughReadableMiddlePacket {

	public InventorySelectEnchant() {
		super(LegacyPacketId.Serverbound.PLAY_INVENTORY_SELECT_ENCHANT, Byte.BYTES * 2);
	}

}
