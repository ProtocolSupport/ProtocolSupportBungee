package protocolsupport.protocol.packet.handler;

import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.DownstreamBridge;
import net.md_5.bungee.protocol.PacketWrapper;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.entitymap.EntityMap;

public class EntityRewriteDownstreamBridge extends DownstreamBridge {

	private final UserConnection con;
	private final EntityMap entitymap;
	public EntityRewriteDownstreamBridge(ProxyServer bungee, UserConnection con) {
		super(bungee, con, con.getServer());
		this.con = con;
		this.entitymap = EntityMap.get(ConnectionImpl.getFromChannel(((PSInitialHandler) con.getPendingConnection()).getChannelWrapper().getHandle()).getVersion());
	}

	@Override
	public void handle(PacketWrapper packet) throws Exception {
		entitymap.rewriteServerbound(packet.buf, con.getClientEntityId(), con.getServerEntityId());
		con.sendPacket(packet);
	}

}
