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

	public PacketWrapper rewrite(PacketWrapper packet, IntUnaryOperator rewritefunc, ByteBuf scratchBuffer) {
		ByteBuf buf = packet.buf;
		buf.markReaderIndex();
		scratchBuffer.clear();
		int packetId = readPacketId(buf);
		EntityRewriteCommand[] chain = rewritechains[packetId];
		if (chain == null) {
			buf.resetReaderIndex();
			return packet;
		}
		try {
			writePacketId(scratchBuffer, packetId);
			for (EntityRewriteCommand command : chain) {
				command.rewrite(buf, scratchBuffer, rewritefunc);
			}
			buf.clear();
			if (buf.maxWritableBytes() < scratchBuffer.readableBytes()) {
				packet.trySingleRelease();
				ByteBuf outBuf = Allocator.allocateBuffer();
				outBuf.writeBytes(scratchBuffer);
				return new PacketWrapper(packet.packet, outBuf);
			} else {
				buf.writeBytes(scratchBuffer);
				return packet;
			}
		} catch(Exception e) {
			System.err.println("Entity remap error in packet ID " + packetId);
			throw e;
		}
	}

	protected abstract int readPacketId(ByteBuf from);

	protected abstract void writePacketId(ByteBuf to, int packetId);

}
