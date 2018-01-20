package protocolsupport.protocol.pipeline.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import protocolsupport.protocol.serializer.VarNumberSerializer;

public class EncapsulatedConnectionKeepAlive  extends IdleStateHandler {

	public EncapsulatedConnectionKeepAlive() {
		super(0, 5, 0);
	}

	@Override
	protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
		ByteBuf rawemptyframedpacket = ctx.alloc().buffer();
		VarNumberSerializer.writeVarInt(rawemptyframedpacket, 0);
		ctx.writeAndFlush(rawemptyframedpacket);
	}

}
