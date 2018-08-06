package protocolsupport.protocol.pipeline.common;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.md_5.bungee.api.ProxyServer;
import protocolsupport.api.events.ConnectionCloseEvent;
import protocolsupport.api.events.ConnectionOpenEvent;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.storage.ProtocolStorage;

public class LogicHandler extends ChannelDuplexHandler {

	protected final ConnectionImpl connection;
	protected final boolean isClientConnection;
	public LogicHandler(ConnectionImpl connection, boolean isClientConnection) {
		this.connection = connection;
		this.isClientConnection = isClientConnection;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		msg = connection.handlePacketReceive(msg, isClientConnection);
		if (msg == null) {
			return;
		}
		super.channelRead(ctx, msg);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		msg = connection.handlePacketSend(msg, isClientConnection);
		if (msg == null) {
			promise.setSuccess();
			return;
		}
		super.write(ctx, msg, promise);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		if (isClientConnection) {
			ProxyServer.getInstance().getPluginManager().callEvent(new ConnectionOpenEvent(connection));
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		if (isClientConnection) {
			ProxyServer.getInstance().getPluginManager().callEvent(new ConnectionCloseEvent(connection));
			ProtocolStorage.removeConnection(connection.getRawAddress());
		}
	}

}
