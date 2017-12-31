package protocolsupport.protocol.packet.handler;

import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.UpstreamBridge;
import net.md_5.bungee.protocol.PacketWrapper;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.entitymap.EntityMap;

public class EntityRewriteUpstreamBridge extends UpstreamBridge {

	private final UserConnection con;
	private final EntityMap entitymap;
	public EntityRewriteUpstreamBridge(ProxyServer bungee, UserConnection con) {
		super(bungee, con);
		this.con = con;
		this.entitymap = EntityMap.get(ConnectionImpl.getFromChannel(((PSInitialHandler) con.getPendingConnection()).getChannelWrapper().getHandle()).getVersion());
	}

	@Override
	public void handle(PacketWrapper packet) throws Exception {
		ServerConnection scon = con.getServer();
		if (scon != null) {
			entitymap.rewriteServerbound(packet.buf, con.getClientEntityId(), con.getServerEntityId());
			scon.getCh().write(packet);
		}
	}

}
