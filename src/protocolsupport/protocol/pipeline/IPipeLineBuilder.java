package protocolsupport.protocol.pipeline;

import io.netty.channel.Channel;
import protocolsupport.api.Connection;

public abstract class IPipeLineBuilder {

	public static final IPipeLineBuilder NOOP = new IPipeLineBuilder() {
		@Override
		public void buildBungeeServer(Channel channel, Connection connection) {
		}
		@Override
		public void buildBungeeClientPipeLine(Channel channel, Connection connection) {
		}
		@Override
		public void buildBungeeClientCodec(Channel channel, Connection connection) {
		}
	};

	public abstract void buildBungeeClientCodec(Channel channel, Connection connection);

	public abstract void buildBungeeClientPipeLine(Channel channel, Connection connection);

	public abstract void buildBungeeServer(Channel channel, Connection connection);

}
