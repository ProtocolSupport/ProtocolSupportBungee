package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5;

import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyFixedLengthPassthroughReadableMiddlePacket;

public class PlayerAbilitiesPacket extends LegacyFixedLengthPassthroughReadableMiddlePacket {

	public PlayerAbilitiesPacket() {
		super(LegacyPacketId.Dualbound.PLAY_ABILITIES, Byte.BYTES * 3);
	}

}
