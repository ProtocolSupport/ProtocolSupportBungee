package protocolsupport.protocol.pipeline.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.utils.netty.Compressor;

public class PacketCompressor extends MessageToByteEncoder<ByteBuf> {

	private final Compressor compressor = Compressor.create();
	private final int threshold;

	public PacketCompressor(int threshold) {
		this.threshold = threshold;
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);
		compressor.recycle();
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf from, ByteBuf to)  {
		int readable = from.readableBytes();
		if (readable == 0) {
			return;
		}
		if (readable < this.threshold) {
			VarNumberSerializer.writeVarInt(to, 0);
			to.writeBytes(from);
		} else {
			VarNumberSerializer.writeVarInt(to, readable);
			to.writeBytes(compressor.compress(MiscSerializer.readAllBytes(from)));
		}
	}

}
