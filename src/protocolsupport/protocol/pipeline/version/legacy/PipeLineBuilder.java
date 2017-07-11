package protocolsupport.protocol.pipeline.version.legacy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.DownstreamBridge;
import net.md_5.bungee.connection.UpstreamBridge;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.Protocol;
import protocolsupport.api.Connection;
import protocolsupport.injector.NettyInjector.CustomHandlerBoss;
import protocolsupport.protocol.pipeline.IPipeLineBuilder;
import protocolsupport.protocol.pipeline.common.NoOpFrameDecoder;
import protocolsupport.protocol.pipeline.common.NoOpFrameEncoder;
import protocolsupport.protocol.pipeline.version.legacy.handler.EntityRewriteDownstreamBridge;
import protocolsupport.protocol.pipeline.version.legacy.handler.EntityRewriteUpstreamBridge;
import protocolsupport.utils.ReflectionUtils;

public class PipeLineBuilder extends IPipeLineBuilder {

	@Override
	public void buildBungeeClient(Channel channel, Connection connection) {
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.replace(PipelineUtils.FRAME_DECODER, PipelineUtils.FRAME_DECODER, new NoOpFrameDecoder());
		pipeline.replace(PipelineUtils.FRAME_PREPENDER, PipelineUtils.FRAME_PREPENDER, new NoOpFrameEncoder());
		pipeline.replace(PipelineUtils.PACKET_DECODER, PipelineUtils.PACKET_DECODER, new PacketDecoder(true, connection.getVersion()));
		pipeline.replace(PipelineUtils.PACKET_ENCODER, PipelineUtils.PACKET_ENCODER, new PacketEncoder(Protocol.HANDSHAKE, true, connection.getVersion()));
		pipeline.get(CustomHandlerBoss.class).setPacketHandlerChangeListener(listener -> {
			try {
				return (listener instanceof UpstreamBridge) ? new EntityRewriteUpstreamBridge(ProxyServer.getInstance(), ReflectionUtils.getFieldValue(listener, "con")) : listener;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		});
	}

	@Override
	public void buildBungeeServer(Channel channel, Connection connection) {
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.replace(PipelineUtils.FRAME_DECODER, PipelineUtils.FRAME_DECODER, new NoOpFrameDecoder());
		pipeline.replace(PipelineUtils.FRAME_PREPENDER, PipelineUtils.FRAME_PREPENDER, new NoOpFrameEncoder());
		pipeline.replace(PipelineUtils.PACKET_DECODER, PipelineUtils.PACKET_DECODER, new PacketDecoder(false, connection.getVersion()));
		pipeline.replace(PipelineUtils.PACKET_ENCODER, PipelineUtils.PACKET_ENCODER, new PacketEncoder(Protocol.HANDSHAKE, false, connection.getVersion()));
		pipeline.get(CustomHandlerBoss.class).setPacketHandlerChangeListener(listener -> {
			try {
				return (listener instanceof DownstreamBridge) ? new EntityRewriteDownstreamBridge(ProxyServer.getInstance(), ReflectionUtils.getFieldValue(listener, "con")) : listener;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		});
	}

}
