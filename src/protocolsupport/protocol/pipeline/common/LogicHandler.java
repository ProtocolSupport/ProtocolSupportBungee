package protocolsupport.protocol.pipeline.common;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.md_5.bungee.api.ProxyServer;
import protocolsupport.api.events.ConnectionCloseEvent;
import protocolsupport.api.events.ConnectionOpenEvent;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.storage.ProtocolStorage;

public class LogicHandler extends ChannelDuplexHandler {

	private final ConnectionImpl connection;
	public LogicHandler(ConnectionImpl connection) {
		this.connection = connection;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		ProxyServer.getInstance().getPluginManager().callEvent(new ConnectionOpenEvent(connection));
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		ProxyServer.getInstance().getPluginManager().callEvent(new ConnectionCloseEvent(connection));
		ProtocolStorage.removeConnection(connection.getAddress());
	}

}
