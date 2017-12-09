package protocolsupport.protocol.pipeline.version.v_pe;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.DownstreamBridge;
import net.md_5.bungee.connection.UpstreamBridge;
import net.md_5.bungee.netty.PipelineUtils;
import protocolsupport.api.Connection;
import protocolsupport.injector.BungeeNettyChannelInjector.CustomHandlerBoss;
import protocolsupport.protocol.packet.handler.PEEntityRewriteDownstreamBridge;
import protocolsupport.protocol.packet.handler.PEEntityRewriteUpstreamBridge;
import protocolsupport.protocol.pipeline.IPipeLineBuilder;
import protocolsupport.protocol.pipeline.common.PacketCompressor;
import protocolsupport.protocol.pipeline.common.PacketDecompressor;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.storage.NetworkDataCache;
import protocolsupport.protocol.utils.EncapsulatedProtocolUtils;
import protocolsupport.utils.ReflectionUtils;

public class PipeLineBuilder extends IPipeLineBuilder {

	@Override
	public void buildBungeeClientCodec(Channel channel, Connection connection) {
		ChannelPipeline pipeline = channel.pipeline();
		NetworkDataCache cache = new NetworkDataCache();
		pipeline.replace(PipelineUtils.PACKET_DECODER, PipelineUtils.PACKET_DECODER, new FromClientPacketDecoder(connection, cache));
		pipeline.replace(PipelineUtils.PACKET_ENCODER, PipelineUtils.PACKET_ENCODER, new ToClientPacketEncoder(connection, cache));
		pipeline.get(CustomHandlerBoss.class).setPacketHandlerChangeListener(listener -> {
			try {
				return (listener instanceof UpstreamBridge) ? new PEEntityRewriteUpstreamBridge(ProxyServer.getInstance(), ReflectionUtils.getFieldValue(listener, "con")) : listener;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		});
	}

	@Override
	public void buildBungeeClientPipeLine(Channel channel, Connection connection) {
		throw new UnsupportedOperationException("Only connection through encapsulation protocol is supported");
	}

	@Override
	public void buildBungeeServer(Channel channel, Connection connection) {
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.addFirst(new ChannelInboundHandlerAdapter() {
			@Override
			public void channelActive(ChannelHandlerContext ctx) throws Exception {
				ctx.writeAndFlush(EncapsulatedProtocolUtils.createHandshake(null, true));
				super.channelActive(ctx);
			}
		});
		NetworkDataCache cache = new NetworkDataCache();
		pipeline.addFirst("idlestatehandler", new IdleStateHandler(0, 5, 0));
		pipeline.addAfter("idlestatehandler", "keepalive", new ChannelInboundHandlerAdapter() {
			@Override
			public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
				if (evt instanceof IdleStateEvent) {
					if (((IdleStateEvent) evt).state() == IdleState.WRITER_IDLE) {
						ByteBuf rawemptyframedpacket = Unpooled.buffer();
						VarNumberSerializer.writeVarInt(rawemptyframedpacket, 0);
						ctx.writeAndFlush(rawemptyframedpacket);
					}
				}
			}
		});
		pipeline.replace(PipelineUtils.PACKET_DECODER, PipelineUtils.PACKET_DECODER, new FromServerPacketDecoder(connection, cache));
		pipeline.replace(PipelineUtils.PACKET_ENCODER, PipelineUtils.PACKET_ENCODER, new ToServerPacketEncoder(connection, cache));
		pipeline.addAfter(PipelineUtils.FRAME_PREPENDER, "compress", new PacketCompressor(256));
		pipeline.addAfter(PipelineUtils.FRAME_DECODER, "decompress", new PacketDecompressor());
		pipeline.get(CustomHandlerBoss.class).setPacketHandlerChangeListener(listener -> {
			try {
				return (listener instanceof DownstreamBridge) ? new PEEntityRewriteDownstreamBridge(ProxyServer.getInstance(), ReflectionUtils.getFieldValue(listener, "con")) : listener;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		});
	}

}
