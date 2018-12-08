package protocolsupport.protocol.packet.handler;

import java.util.function.IntUnaryOperator;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.UpstreamBridge;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.protocol.PacketWrapper;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.packet.entityrewrite.EntityRewrite;
import protocolsupport.protocol.packet.entityrewrite.EntityRewriteFactory;
import protocolsupport.utils.netty.Allocator;

public class EntityRewriteUpstreamBridge extends UpstreamBridge {

	protected final UserConnection con;
	protected final IntUnaryOperator rewritefunc;
	protected final EntityRewrite rewrite;
	protected final ByteBuf scratchBuffer = Allocator.allocateBuffer();

	public EntityRewriteUpstreamBridge(UserConnection con, ProtocolVersion version) {
		super(ProxyServer.getInstance(), con);
		this.con = con;
		this.rewritefunc = EntityRewriteFactory.createReplaceEntityIdFunc(con::getClientEntityId, con::getServerEntityId);
		this.rewrite = EntityRewriteFactory.getFromClientRewrite(version);
	}

	@Override
	public void handle(PacketWrapper packet) throws Exception {
		if (con.getServer() != null) {
			con.getServer().getCh().write(rewrite.rewrite(packet, rewritefunc, scratchBuffer));
		}
	}

	@Override
	public void disconnected(ChannelWrapper channel) throws Exception {
		super.disconnected(channel);
		scratchBuffer.release();
	}
}
