package protocolsupport.protocol.transformer.v_1_6.handlers;

import protocolsupport.protocol.transformer.v_1_6.entityrewrite.ServerboundEntityRewrite;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.UpstreamBridge;
import net.md_5.bungee.protocol.PacketWrapper;

public class EntityRewriteUpstreamBridge extends UpstreamBridge {

	private UserConnection con;
	public EntityRewriteUpstreamBridge(ProxyServer bungee, UserConnection con) {
		super(bungee, con);
		this.con = con;
	}

	private ServerboundEntityRewrite rewrite = new ServerboundEntityRewrite();

	public void handle(PacketWrapper packet) throws Exception {
		rewrite.rewriteServerbound(packet.buf, this.con.getClientEntityId(), this.con.getServerEntityId());
		if (this.con.getServer() != null) {
			this.con.getServer().getCh().write(packet);
		}
	}

}
