package protocolsupport.protocol.packet.middleimpl.readable;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import protocolsupport.protocol.packet.middle.ReadableMiddlePacket;

public abstract class FixedLengthPassthroughReadableMiddlePacket extends ReadableMiddlePacket {

	protected final int packetId;
	protected final int length;
	public FixedLengthPassthroughReadableMiddlePacket(int packetId, int length) {
		this.packetId = packetId;
		this.length = length;
	}

	protected byte[] bytes;

	@Override
	public void read(ByteBuf data) {
		bytes = new byte[length + 1];
		bytes[0] = (byte) packetId;
		data.readBytes(bytes, 1, length);
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(null, Unpooled.wrappedBuffer(bytes)));
	}

}
