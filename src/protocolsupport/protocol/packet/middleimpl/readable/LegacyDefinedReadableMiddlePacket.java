package protocolsupport.protocol.packet.middleimpl.readable;

import io.netty.buffer.ByteBuf;

public abstract class LegacyDefinedReadableMiddlePacket extends DefinedReadableMiddlePacket {

	public LegacyDefinedReadableMiddlePacket(int packetId) {
		super(packetId);
	}

	@Override
	protected void writePacketId(ByteBuf to) {
		to.writeByte(packetId);
	}

}
