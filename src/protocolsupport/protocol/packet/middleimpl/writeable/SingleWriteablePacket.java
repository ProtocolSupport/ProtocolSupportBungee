package protocolsupport.protocol.packet.middleimpl.writeable;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.DefinedPacket;
import protocolsupport.protocol.packet.middle.WriteableMiddlePacket;
import protocolsupport.utils.netty.Allocator;

public abstract class SingleWriteablePacket<T extends DefinedPacket> extends WriteableMiddlePacket<T> {

	protected final int packetId;
	public SingleWriteablePacket(int packetId) {
		this.packetId = packetId;
	}

	@Override
	public Collection<ByteBuf> toData(T packet) {
		ByteBuf data = Allocator.allocateBuffer();
		data.writeByte(packetId);
		write(data, packet);
		return Collections.singletonList(data);
	}

	protected abstract void write(ByteBuf data, T packet);

}
