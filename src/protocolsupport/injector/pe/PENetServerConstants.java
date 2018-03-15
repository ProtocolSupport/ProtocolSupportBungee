package protocolsupport.injector.pe;

import io.netty.channel.Channel;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.api.events.PocketServerInfoEvent;
import raknetserver.pipeline.raknet.RakNetPacketConnectionEstablishHandler.PingHandler;


public class PENetServerConstants {

	public static final PingHandler PING_HANDLER = new PingHandler() {
		@Override
		public String getServerInfo(Channel channel) {
            PocketServerInfoEvent event = PocketServerInfoEvent.call("ProtocolSupportBungee");
            return String.join(";",
				"MCPE",
                event.getWelcomeMessage(),
				String.valueOf(ProtocolVersion.MINECRAFT_PE.getId()), POCKET_VERSION,
				String.valueOf(event.getOnline()), String.valueOf(event.getServerSize())
			);
		}
		@Override
		public void executeHandler(Runnable runnable) {
			runnable.run();
		}
	};

	public static final int USER_PACKET_ID = 0xFE;
	public static final String POCKET_VERSION = "1.2.7";

}
