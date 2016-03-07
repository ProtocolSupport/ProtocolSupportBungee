package protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.handlers;

import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.UpstreamBridge;
import net.md_5.bungee.protocol.PacketWrapper;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.entityrewrite.ServerboundEntityRewrite;

public class EntityRewriteUpstreamBridge extends UpstreamBridge {

	private UserConnection con;
	public EntityRewriteUpstreamBridge(ProxyServer bungee, UserConnection con) {
		super(bungee, con);
		this.con = con;
	}

	@Override
	public void handle(PacketWrapper packet) throws Exception {
		ServerboundEntityRewrite.rewriteServerbound(packet.buf, this.con.getClientEntityId(), this.con.getServerEntityId());
		if (this.con.getServer() != null) {
			this.con.getServer().getCh().write(packet);
		}
	}

}
