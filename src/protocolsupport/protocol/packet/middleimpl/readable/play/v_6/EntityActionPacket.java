package protocolsupport.protocol.packet.middleimpl.readable.play.v_6;

import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyFixedLengthPassthroughReadableMiddlePacket;

public class EntityActionPacket extends LegacyFixedLengthPassthroughReadableMiddlePacket {

	public EntityActionPacket() {
		super(LegacyPacketId.Dualbound.PLAY_ENTITY_ACTION, Integer.BYTES + Byte.BYTES + Integer.BYTES);
	}

}
