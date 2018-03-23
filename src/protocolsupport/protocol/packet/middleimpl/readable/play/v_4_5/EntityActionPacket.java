package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5;

import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyFixedLengthPassthroughReadableMiddlePacket;

public class EntityActionPacket extends LegacyFixedLengthPassthroughReadableMiddlePacket {

	public EntityActionPacket() {
		super(LegacyPacketId.Serverbound.PLAY_ENTITY_ACTION, Integer.BYTES + Byte.BYTES);
	}

}
