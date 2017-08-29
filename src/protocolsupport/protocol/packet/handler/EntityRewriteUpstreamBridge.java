package protocolsupport.protocol.packet.handler;

import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.UpstreamBridge;
import net.md_5.bungee.protocol.PacketWrapper;
import protocolsupport.protocol.entitymap.LegacyEntityMap;

public class EntityRewriteUpstreamBridge extends UpstreamBridge {

	private final UserConnection con;
	public EntityRewriteUpstreamBridge(ProxyServer bungee, UserConnection con) {
		super(bungee, con);
		this.con = con;
	}

	@Override
	public void handle(PacketWrapper packet) throws Exception {
		if (this.con.getServer() != null) {
			LegacyEntityMap.rewriteServerbound(packet.buf, this.con.getClientEntityId(), this.con.getServerEntityId());
			this.con.getServer().getCh().write(packet);
		}
	}

}
