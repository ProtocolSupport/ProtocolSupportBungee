package protocolsupport.protocol.packet.entityrewrite;

import java.util.function.IntUnaryOperator;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketWrapper;
import protocolsupport.utils.netty.Allocator;

public abstract class EntityRewrite {

	protected final EntityRewriteCommand[][] rewritechains = new EntityRewriteCommand[256][];

	public void register(int packetId, EntityRewriteCommand... chain) {
		rewritechains[packetId] = chain.clone();
	}

	public PacketWrapper rewrite(PacketWrapper packet, IntUnaryOperator rewritefunc) {
		ByteBuf buf = packet.buf;
		buf.markReaderIndex();
		int packetId = readPacketId(buf);
		EntityRewriteCommand[] chain = rewritechains[packetId];
		if (chain == null) {
			buf.resetReaderIndex();
			return packet;
		}
		ByteBuf tmpBuffer = Allocator.allocateBuffer();
		try {
			writePacketId(tmpBuffer, packetId);
			for (EntityRewriteCommand command : chain) {
				command.rewrite(buf, tmpBuffer, rewritefunc);
			}
			buf.clear();
			if (buf.maxWritableBytes() < tmpBuffer.readableBytes()) {
				packet.trySingleRelease();
				return new PacketWrapper(packet.packet, tmpBuffer);
			} else {
				buf.writeBytes(tmpBuffer);
				tmpBuffer.release();
				return packet;
			}
		} catch (Exception e) {
			tmpBuffer.release();
			throw new RuntimeException("Entity remap error in packet ID " + packetId, e);
		}
	}

	protected abstract int readPacketId(ByteBuf from);

	protected abstract void writePacketId(ByteBuf to, int packetId);

}
