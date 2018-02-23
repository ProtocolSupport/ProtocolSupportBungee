package protocolsupport.protocol.packet.middleimpl.readable.play.v_6;

import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyFixedLengthPassthroughReadableMiddlePacket;

public class VehicleControlPacket extends LegacyFixedLengthPassthroughReadableMiddlePacket {

	public VehicleControlPacket() {
		super(LegacyPacketId.Serverbound.PLAY_VEHICLE_CONTROL, (Float.BYTES * 2) + (Byte.BYTES * 2));
	}

}
