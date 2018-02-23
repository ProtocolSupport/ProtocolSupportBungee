package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyFixedLengthPassthroughReadableMiddlePacket;

public class BlockDigPacket extends LegacyFixedLengthPassthroughReadableMiddlePacket {

	public BlockDigPacket() {
		super(LegacyPacketId.Serverbound.PLAY_BLOCK_DIG, Byte.BYTES + Integer.BYTES + Byte.BYTES + Integer.BYTES + Byte.BYTES);
	}

}
