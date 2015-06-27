package protocolsupport.protocol.transformer.v_1_7;

import net.md_5.bungee.netty.PipelineUtils;
import io.netty.channel.Channel;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.listeners.IPipeLineBuilder;

public class PipeLineBuilder implements IPipeLineBuilder {

	@Override
	public void buildPipeLine(Channel channel, ProtocolVersion version) {
		channel.pipeline().replace(PipelineUtils.FRAME_DECODER, PipelineUtils.FRAME_DECODER, new FrameDecoder());
	}

}
