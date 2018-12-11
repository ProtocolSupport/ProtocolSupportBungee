package protocolsupport.protocol.packet.handler;

import java.util.function.IntUnaryOperator;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.DownstreamBridge;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.PluginMessage;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.packet.entityrewrite.EntityRewrite;
import protocolsupport.protocol.packet.entityrewrite.EntityRewriteFactory;
import protocolsupport.utils.netty.Allocator;

public class EntityRewriteDownstreamBridge extends DownstreamBridge {

	protected final UserConnection con;
	protected final IntUnaryOperator rewritefunc;
	protected final EntityRewrite rewrite;

	public EntityRewriteDownstreamBridge(UserConnection con, ProtocolVersion version) {
		super(ProxyServer.getInstance(), con, con.getServer());
		this.con = con;
		this.rewritefunc = EntityRewriteFactory.createReplaceEntityIdFunc(con::getClientEntityId, con::getServerEntityId);
		this.rewrite = EntityRewriteFactory.getToClientRewrite(version);
	}

	@Override
	public void handle(PacketWrapper packet) throws Exception {
		con.sendPacket(rewrite.rewrite(packet, rewritefunc));
	}

	@Override
	public void handle(PluginMessage pluginMessage) throws Exception {
		//TODO: bungee somehow doesnt update these, even though our protocol technically supports the new format
		if (pluginMessage.getTag().equals("bungeecord:main")) {
			pluginMessage.setTag("BungeeCord");
		}
		super.handle(pluginMessage);
	}

}
