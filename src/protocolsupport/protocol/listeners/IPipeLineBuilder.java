package protocolsupport.protocol.listeners;

import io.netty.channel.Channel;
import protocolsupport.api.ProtocolVersion;

public interface IPipeLineBuilder {

	public void buildPipeLine(Channel channel, ProtocolVersion version);

}
