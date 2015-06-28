package protocolsupport.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.MinecraftEncoder;
import net.md_5.bungee.protocol.Protocol;

public class CheckedChannelWrapper extends ChannelWrapper {

	private Channel ch;
	public CheckedChannelWrapper(ChannelHandlerContext ctx) {
		super(ctx);
		ch = ctx.channel();
	}

	@Override
	public void setProtocol(final Protocol protocol) {
		ChannelHandler handler = this.ch.pipeline().get(PipelineUtils.PACKET_DECODER);
		if (handler instanceof MinecraftDecoder) {
			((MinecraftDecoder) handler).setProtocol(protocol);
		}
		this.ch.pipeline().get(MinecraftEncoder.class).setProtocol(protocol);
	}

	@Override
	public void setVersion(final int protocol) {
		ChannelHandler handler = this.ch.pipeline().get(PipelineUtils.PACKET_DECODER);
		if (handler instanceof MinecraftDecoder) {
			((MinecraftDecoder) handler).setProtocolVersion(protocol);
		}
		this.ch.pipeline().get(MinecraftEncoder.class).setProtocolVersion(protocol);
	}

}
