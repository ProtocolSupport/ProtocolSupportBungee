package protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.handlers;

import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.DownstreamBridge;
import net.md_5.bungee.protocol.PacketWrapper;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.entityrewrite.ClientboundEntityRewrite;

public class EntityRewriteDownstreamBridge extends DownstreamBridge {

	private UserConnection con;
	private ServerConnection server;
	public EntityRewriteDownstreamBridge(ProxyServer bungee, UserConnection con, ServerConnection server) {
		super(bungee, con, server);
		this.con = con;
		this.server = server;
	}

	private ClientboundEntityRewrite rewrite = new ClientboundEntityRewrite();

	@Override
	public void handle(PacketWrapper packet) throws Exception {
		if (!this.server.isObsolete()) {
			rewrite.rewriteClientbound(packet.buf, this.con.getServerEntityId(), this.con.getClientEntityId());
			this.con.sendPacket(packet);
		}
	}

}
