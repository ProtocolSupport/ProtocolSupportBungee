package protocolsupport.protocol.pipeline.common;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protocolsupport.protocol.utils.EncapsulatedProtocolInfo;
import protocolsupport.protocol.utils.EncapsulatedProtocolUtils;

public class EncapsulatedHandshakeSender extends ChannelInboundHandlerAdapter {

	private final InetSocketAddress remote;
	private final boolean hasCompression;
	public EncapsulatedHandshakeSender(InetSocketAddress remote, boolean hasCompression) {
		this.remote = remote;
		this.hasCompression = hasCompression;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ByteBuf handshake = ctx.alloc().buffer();
		handshake.writeByte(EncapsulatedProtocolUtils.FIRST_BYTE);
		EncapsulatedProtocolUtils.writeInfo(handshake, new EncapsulatedProtocolInfo(remote, hasCompression));
		ctx.writeAndFlush(handshake).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
		super.channelActive(ctx);
	}

}
