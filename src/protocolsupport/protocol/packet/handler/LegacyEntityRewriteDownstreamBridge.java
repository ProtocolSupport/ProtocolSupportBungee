package protocolsupport.protocol.packet.handler;

import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.DownstreamBridge;
import net.md_5.bungee.protocol.PacketWrapper;
import protocolsupport.protocol.entitymap.LegacyEntityMap;

public class LegacyEntityRewriteDownstreamBridge extends DownstreamBridge {

	private final UserConnection con;
	public LegacyEntityRewriteDownstreamBridge(ProxyServer bungee, UserConnection con) {
		super(bungee, con, con.getServer());
		this.con = con;
	}

	@Override
	public void handle(PacketWrapper packet) throws Exception {
		LegacyEntityMap.rewriteServerbound(packet.buf, this.con.getClientEntityId(), this.con.getServerEntityId());
		this.con.sendPacket(packet);
	}

}
