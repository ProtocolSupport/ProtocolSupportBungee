package protocolsupport.protocol.packet.entityrewrite;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.serializer.PEPacketIdSerializer;

public abstract class PEEntityRewrite extends EntityRewrite {

	@Override
	protected int readPacketId(ByteBuf from) {
		return PEPacketIdSerializer.readPacketId(from);
	}

	@Override
	protected void writePacketId(ByteBuf to, int packetId) {
		PEPacketIdSerializer.writePacketId(to, packetId);
	}

}
