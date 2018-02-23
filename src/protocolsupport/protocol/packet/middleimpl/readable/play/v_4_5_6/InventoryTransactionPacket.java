package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyFixedLengthPassthroughReadableMiddlePacket;

public class InventoryTransactionPacket extends LegacyFixedLengthPassthroughReadableMiddlePacket {

	public InventoryTransactionPacket() {
		super(LegacyPacketId.Dualbound.PLAY_INVENTORY_TRANSACTION, Byte.BYTES + Short.BYTES + Byte.BYTES);
	}

}
