package protocolsupport.protocol.packet.middleimpl.writeable;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.DefinedPacket;

public abstract class LegacySingleWriteablePacket<T extends DefinedPacket> extends SingleWriteablePacket<T> {

	public LegacySingleWriteablePacket(int packetId) {
		super(packetId);
	}

	@Override
	protected void writePacketId(ByteBuf data) {
		data.writeByte(packetId);
	}

}
