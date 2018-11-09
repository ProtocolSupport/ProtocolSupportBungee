package protocolsupport.injector.pe;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.md_5.bungee.BungeeCord;
import raknetserver.RakNetServer;
import raknetserver.RakNetServer.UserChannelInitializer;

public class PEProxyServer {

	private final RakNetServer peserver = new RakNetServer(
		BungeeCord.getInstance().getConfig().getListeners().iterator().next().getHost(),
		new PEProxyServerInfoHandler(),
		new UserChannelInitializer() {
			@Override
			public void init(Channel channel) {
				PEQueryHandler queryHandler = new PEQueryHandler();
				ChannelPipeline pipeline = channel.pipeline();
				pipeline.addFirst(queryHandler);
				pipeline.addFirst(queryHandler.createWriter());
				pipeline.addLast(new PECompressor());
				pipeline.addLast(new PEDecompressor());
				pipeline.addLast(PEProxyNetworkManager.NAME, new PEProxyNetworkManager());
			}
		}, 0xFE
	);

	public void start() {
		peserver.start();
	}

	public void stop() {
		peserver.stop();
	}

}
