package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyFixedLengthPassthroughReadableMiddlePacket;

public class PlayerLookPacket extends LegacyFixedLengthPassthroughReadableMiddlePacket {

	public PlayerLookPacket() {
		super(LegacyPacketId.Serverbound.PLAY_LOOK, (Float.BYTES * 2) + Byte.BYTES);
	}

}
