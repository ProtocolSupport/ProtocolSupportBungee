package protocolsupport.injector.pe;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.md_5.bungee.BungeeCord;
import raknetserver.RakNetServer;
import raknetserver.RakNetServer.UserChannelInitializer;

public class PEProxyServer {

	private final RakNetServer peserver = new RakNetServer(
		BungeeCord.getInstance().getConfig().getListeners().iterator().next().getHost(),
		PENetServerConstants.PING_HANDLER,
		new UserChannelInitializer() {
			@Override
			public void init(Channel channel) {
				ChannelPipeline pipeline = channel.pipeline();
				pipeline.addLast(new PECompressor());
				pipeline.addLast(new PEDecompressor());
				pipeline.addLast(new PEProxyNetworkManager());
			}
		}, PENetServerConstants.USER_PACKET_ID
	);

	public void start() {
		peserver.start();
	}

	public void stop() {
		peserver.stop();
	}

}
