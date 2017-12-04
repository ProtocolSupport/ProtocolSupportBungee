package protocolsupport.protocol.packet.handler;

import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.DownstreamBridge;
import net.md_5.bungee.protocol.PacketWrapper;

public class PEEntityRewriteDownstreamBridge extends DownstreamBridge {

	private final UserConnection con;
	public PEEntityRewriteDownstreamBridge(ProxyServer bungee, UserConnection con) {
		super(bungee, con, con.getServer());
		this.con = con;
	}

	@Override
	public void handle(PacketWrapper packet) throws Exception {
		this.con.sendPacket(packet);
	}

}
