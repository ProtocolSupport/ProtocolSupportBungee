package protocolsupport.protocol.packet.middleimpl.readable;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import protocolsupport.protocol.packet.middle.ReadableMiddlePacket;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.utils.netty.Allocator;

public abstract class DynamicLengthPassthroughReadableMiddlePacket extends ReadableMiddlePacket {

	protected final int packetId;
	public DynamicLengthPassthroughReadableMiddlePacket(int packetId) {
		this.packetId = packetId;
	}

	protected byte[] bytes;

	@Override
	public void read(ByteBuf data) {
		ByteBuf buffer = Allocator.allocateBuffer();
		try {
			buffer.writeByte(packetId);
			readTo(data, buffer);
			bytes = MiscSerializer.readAllBytes(buffer);
		} finally {
			buffer.release();
		}
	}

	protected abstract void readTo(ByteBuf data, ByteBuf to);

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(null, Unpooled.wrappedBuffer(bytes)));
	}

}
