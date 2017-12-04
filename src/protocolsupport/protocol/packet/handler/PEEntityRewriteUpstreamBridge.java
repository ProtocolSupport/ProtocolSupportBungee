package protocolsupport.protocol.packet.handler;

import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.UpstreamBridge;
import net.md_5.bungee.protocol.PacketWrapper;

public class PEEntityRewriteUpstreamBridge extends UpstreamBridge {

	private final UserConnection con;
	public PEEntityRewriteUpstreamBridge(ProxyServer bungee, UserConnection con) {
		super(bungee, con);
		this.con = con;
	}

	@Override
	public void handle(PacketWrapper packet) throws Exception {
		if (this.con.getServer() != null) {
			this.con.getServer().getCh().write(packet);
		}
	}

}
