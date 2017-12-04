package protocolsupport.protocol.packet.middleimpl.writeable;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.DefinedPacket;
import protocolsupport.protocol.serializer.VarNumberSerializer;

public abstract class PESingleWriteablePacket<T extends DefinedPacket> extends SingleWriteablePacket<T> {

	public PESingleWriteablePacket(int packetId) {
		super(packetId);
	}

	@Override
	protected void writePacketId(ByteBuf data) {
		VarNumberSerializer.writeVarInt(data, packetId);
		data.writeByte(0);
		data.writeByte(0);
	}

}
