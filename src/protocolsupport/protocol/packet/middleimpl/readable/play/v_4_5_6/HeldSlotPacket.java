package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyFixedLengthPassthroughReadableMiddlePacket;

public class HeldSlotPacket extends LegacyFixedLengthPassthroughReadableMiddlePacket {

	public HeldSlotPacket() {
		super(LegacyPacketId.Serverbound.PLAY_HELD_SLOT, Short.BYTES);
	}

}
