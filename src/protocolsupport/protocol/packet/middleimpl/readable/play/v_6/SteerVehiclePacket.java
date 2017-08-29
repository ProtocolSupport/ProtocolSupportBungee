package protocolsupport.protocol.packet.middleimpl.readable.play.v_6;

import protocolsupport.protocol.packet.middleimpl.readable.FixedLengthPassthroughReadableMiddlePacket;

public class SteerVehiclePacket extends FixedLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0x1B;

	public SteerVehiclePacket() {
		super(PACKET_ID, (Float.BYTES * 2) + (Byte.BYTES * 2));
	}

}
