package protocolsupport.injector;

import java.util.Map.Entry;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.haproxy.HAProxyMessageDecoder;
import net.md_5.bungee.ServerConnector;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.PacketHandler;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.Varint21LengthFieldPrepender;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.pipeline.ChannelHandlers;
import protocolsupport.protocol.pipeline.IPipeLineBuilder;
import protocolsupport.protocol.pipeline.common.LogicHandler;
import protocolsupport.protocol.pipeline.initial.InitialPacketDecoder;
import protocolsupport.protocol.storage.ProtocolStorage;
import protocolsupport.utils.ReflectionUtils;
import protocolsupport.utils.netty.ChannelInitializer;

//yep, thats our entry point, a single static field
public class NettyInjector extends Varint21LengthFieldPrepender {

	private NettyInjector() {
	}

	public static void inject() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		ReflectionUtils.setStaticFinalField(PipelineUtils.class.getDeclaredField("framePrepender"), new NettyInjector());
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		super.handlerAdded(ctx);
		ctx.channel().pipeline().addFirst(new ChannelInitializerEntryPoint());
	}

	private static class ChannelInitializerEntryPoint extends ChannelInitializer {

		@Override
		protected void initChannel(Channel channel) throws Exception {
			ChannelPipeline pipeline = channel.pipeline();
			PacketHandler handler = ReflectionUtils.getFieldValue(pipeline.get(HandlerBoss.class), "handler");
			CustomHandlerBoss boss = new CustomHandlerBoss(handler);
			pipeline.replace(PipelineUtils.BOSS_HANDLER, PipelineUtils.BOSS_HANDLER, boss);
			if (handler instanceof InitialHandler) {//user to bungee connection
				Entry<String, ChannelHandler> firstHandler = pipeline.iterator().next();
				if (firstHandler.getValue() instanceof HAProxyMessageDecoder) {
					pipeline.addAfter(firstHandler.getKey(), ChannelHandlers.INITIAL_DECODER, new InitialPacketDecoder());
				} else {
					pipeline.addFirst(ChannelHandlers.INITIAL_DECODER, new InitialPacketDecoder());
				}
				ConnectionImpl connection = new ConnectionImpl(boss);
				connection.storeInChannel(channel);
				ProtocolStorage.addConnection(channel.remoteAddress(), connection);
				pipeline.addBefore(PipelineUtils.BOSS_HANDLER, ChannelHandlers.LOGIC, new LogicHandler(connection));
				pipeline.remove(PipelineUtils.LEGACY_DECODER);
				pipeline.remove(PipelineUtils.LEGACY_KICKER);
			} else if (handler instanceof ServerConnector) {//bungee to server connection
				UserConnection userconn = ReflectionUtils.getFieldValue(handler, "user");
				Connection connection = ProtocolSupportAPI.getConnection(userconn);
				IPipeLineBuilder builder = IPipeLineBuilder.BUILDERS.get(connection.getVersion());
				if (builder != null) {
					builder.buildBungeeServer(channel, connection);
				}
			}
		}

	}

	public static class CustomHandlerBoss extends HandlerBoss {

		private Channel channel;

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			super.channelActive(ctx);
			channel = ctx.channel();
		}

		public boolean isConnected() {
			return (channel != null) && channel.isActive();
		}

		public Channel getChannel() {
			return channel;
		}

		private PacketHandler handler;

		public CustomHandlerBoss(PacketHandler handler) {
			setHandler(handler);
		}

		@Override
		public void setHandler(PacketHandler handler) {
			handler = packetHandlerChangeListener.change(handler);
			super.setHandler(handler);
			this.handler = handler;
		}

		public PacketHandler getHandler() {
			return this.handler;
		}

		private PacketHandlerChangeListener packetHandlerChangeListener = (listener) -> listener;

		public void setPacketHandlerChangeListener(PacketHandlerChangeListener listener) {
			this.packetHandlerChangeListener = listener;
		}

		@FunctionalInterface
		public static interface PacketHandlerChangeListener {
			public PacketHandler change(PacketHandler handler);
		}

	}

}
