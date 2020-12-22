package protocolsupport.protocol.packet.middleimpl.readable;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.packet.middle.ReadableMiddlePacket;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.utils.netty.Allocator;

public abstract class DefinedReadableMiddlePacket extends ReadableMiddlePacket {

	protected final int packetId;
	public DefinedReadableMiddlePacket(int packetId) {
		this.packetId = packetId;
	}

	protected byte[] readbytes;

	@Override
	public void read(ByteBuf data) {
		int index = data.readerIndex();// Packet id skipped
		data.resetReaderIndex();// Reset reader index to first
		readbytes = MiscSerializer.readAllBytes(data);
		data.readerIndex(index);
		read0(data);
	}

	protected abstract void writePacketId(ByteBuf to);

	protected abstract void read0(ByteBuf from);

}
