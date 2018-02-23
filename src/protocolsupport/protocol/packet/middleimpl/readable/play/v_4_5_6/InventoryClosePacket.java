package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyFixedLengthPassthroughReadableMiddlePacket;

public class InventoryClosePacket extends LegacyFixedLengthPassthroughReadableMiddlePacket {

	public InventoryClosePacket() {
		super(LegacyPacketId.Serverbound.PLAY_INVENTORY_CLOSE, Byte.BYTES);
	}

}
