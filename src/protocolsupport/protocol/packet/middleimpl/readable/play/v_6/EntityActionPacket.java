package protocolsupport.protocol.packet.middleimpl.readable.play.v_6;

import protocolsupport.protocol.packet.middleimpl.readable.FixedLengthPassthroughReadableMiddlePacket;

public class EntityActionPacket extends FixedLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0x13;

	public EntityActionPacket() {
		super(PACKET_ID, Integer.BYTES + Byte.BYTES + Integer.BYTES);
	}

}
