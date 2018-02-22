package protocolsupport.protocol.packet.middleimpl.writeable;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.DefinedPacket;
import protocolsupport.protocol.serializer.PEPacketIdSerializer;

public abstract class PESingleWriteablePacket<T extends DefinedPacket> extends SingleWriteablePacket<T> {

	public PESingleWriteablePacket(int packetId) {
		super(packetId);
	}

	@Override
	protected void writePacketId(ByteBuf data) {
		PEPacketIdSerializer.writePacketId(data, packetId);
	}

}
