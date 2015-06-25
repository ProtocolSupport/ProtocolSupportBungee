package protocolsupport.protocol.listeners.initial;

import java.nio.charset.StandardCharsets;

import net.md_5.bungee.api.ProxyServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

public class OldPingResponseTask implements Runnable {

	private InitialPacketDecoder initialDecoder;
	private Channel channel;

	public OldPingResponseTask(InitialPacketDecoder initialDecoder, Channel channel) {
		this.initialDecoder = initialDecoder;
		this.channel = channel;
	}

	@Override
	public void run() {
		try {
			if (channel.isOpen() && !initialDecoder.protocolSet) {
				@SuppressWarnings("deprecation")
				String response = "BungeeCord"+"§"+ProxyServer.getInstance().getOnlineCount()+"§"+ProxyServer.getInstance().getConfig().getPlayerLimit();
				ByteBuf buf = Unpooled.buffer();
				buf.writeByte(255);
				buf.writeShort(response.length());
				buf.writeBytes(response.getBytes(StandardCharsets.UTF_16BE));
				channel.pipeline().firstContext().writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE);
			}
		} catch (Throwable t) {
			if (channel.isOpen()) {
				channel.close();
			}
		}
	}

}
