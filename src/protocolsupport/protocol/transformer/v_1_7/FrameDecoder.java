package protocolsupport.protocol.transformer.v_1_7;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.Varint21FrameDecoder;

public class FrameDecoder extends Varint21FrameDecoder {

	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		in.markReaderIndex();
		final byte[] buf = new byte[3];
		int i = 0;
		while (i < buf.length) {
			if (!in.isReadable()) {
				in.resetReaderIndex();
				return;
			}
			buf[i] = in.readByte();
			if (buf[i] >= 0) {
				int length = DefinedPacket.readVarInt(Unpooled.wrappedBuffer(buf));
				if (length == 0) {
					return;
				}
				if (in.readableBytes() < length) {
					in.resetReaderIndex();
					return;
				}
				out.add(in.readBytes(length));
				return;
			} else {
				++i;
			}
		}
		throw new CorruptedFrameException("length wider than 21-bit");
	}

}
