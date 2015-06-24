package protocolsupport.protocol.transformer.v_1_5_v1_6_shared;

import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.Protocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.pipeline.IPipeLineBuilder;

public class PipeLineBuilder implements IPipeLineBuilder {

	@Override
	public void buildPipeLine(Channel channel, ProtocolVersion version) {
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.replace(PipelineUtils.FRAME_DECODER, PipelineUtils.FRAME_DECODER, new FakeSplitter());
		pipeline.replace(PipelineUtils.FRAME_PREPENDER, PipelineUtils.FRAME_PREPENDER, new FakePrepender());
		pipeline.replace(PipelineUtils.PACKET_DECODER, PipelineUtils.PACKET_DECODER, new PacketDecoder(Protocol.HANDSHAKE, true, ProtocolVersion.MINECRAFT_1_8.getId(), version));
		pipeline.replace(PipelineUtils.PACKET_ENCODER, PipelineUtils.PACKET_ENCODER, new PacketEncoder(Protocol.HANDSHAKE, true, ProtocolVersion.MINECRAFT_1_8.getId()));
	}

}
