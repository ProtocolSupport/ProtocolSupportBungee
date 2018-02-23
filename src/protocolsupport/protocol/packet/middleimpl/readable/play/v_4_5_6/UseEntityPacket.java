package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyFixedLengthPassthroughReadableMiddlePacket;

public class UseEntityPacket extends LegacyFixedLengthPassthroughReadableMiddlePacket {

	public UseEntityPacket() {
		super(LegacyPacketId.Serverbound.PLAY_USE_ENTITY, Integer.BYTES + Integer.BYTES + Byte.BYTES);
	}

}
