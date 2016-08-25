package protocolsupport.injector;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import protocolsupport.protocol.listeners.initial.InitialPacketDecoder;
import protocolsupport.utils.ReflectionUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.MinecraftEncoder;
import net.md_5.bungee.protocol.Protocol;

public class NettyInjector {

	public static void inject() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		ReflectionUtils.setStaticFinalField(PipelineUtils.class.getDeclaredField("SERVER_CHILD"), new ProxyChannelInitializer());
	}

	private static final class ProxyChannelInitializer extends ChannelInitializer<Channel> {

		@SuppressWarnings("deprecation")
		@Override
		protected void initChannel(Channel channel) throws Exception {
			channel.pipeline().addFirst(InitialPacketDecoder.NAME, new InitialPacketDecoder());
			PipelineUtils.BASE.initChannel(channel);
			channel.pipeline()
			.addAfter(PipelineUtils.FRAME_DECODER, PipelineUtils.PACKET_DECODER, new MinecraftDecoder(Protocol.HANDSHAKE, true, ProxyServer.getInstance().getProtocolVersion()))
			.addAfter(PipelineUtils.FRAME_PREPENDER, PipelineUtils.PACKET_ENCODER, new MinecraftEncoder(Protocol.HANDSHAKE, true, ProxyServer.getInstance().getProtocolVersion()))
			.get(HandlerBoss.class).setHandler(new InitialHandler(BungeeCord.getInstance(), channel.attr(PipelineUtils.LISTENER).get()));
		}
		
	}

}
