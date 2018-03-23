package protocolsupport.protocol.packet.entityrewrite;

import io.netty.buffer.ByteBuf;

public class LegacyEntityRewrite extends EntityRewrite {

	@Override
	protected int readPacketId(ByteBuf from) {
		return from.readUnsignedByte();
	}

	@Override
	protected void writePacketId(ByteBuf to, int packetId) {
		to.writeByte(packetId);
	}

}
