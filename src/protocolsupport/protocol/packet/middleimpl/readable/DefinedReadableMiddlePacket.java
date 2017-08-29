package protocolsupport.protocol.packet.middleimpl.readable;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.packet.middle.ReadableMiddlePacket;

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
		readbytes = new byte[(data.readerIndex() - readerIndex) + 1];
		readbytes[0] = (byte) packetId;
		data.getBytes(readerIndex, readbytes, 1, readbytes.length - 1);
	}

	protected abstract void read0(ByteBuf from);

}
