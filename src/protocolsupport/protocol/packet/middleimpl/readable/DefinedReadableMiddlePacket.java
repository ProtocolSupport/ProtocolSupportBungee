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
		int readerIndex = data.readerIndex();
		read0(data);
		ByteBuf buffer = Allocator.allocateBuffer();
		try {
			writePacketId(buffer);
			int readBytes = data.readerIndex() - readerIndex;
			data.readerIndex(readerIndex);
			buffer.writeBytes(data, readBytes);
			readbytes = MiscSerializer.readAllBytes(buffer);
		} finally {
			buffer.release();
		}
	}

	protected abstract void writePacketId(ByteBuf to);

	protected abstract void read0(ByteBuf from);

}
