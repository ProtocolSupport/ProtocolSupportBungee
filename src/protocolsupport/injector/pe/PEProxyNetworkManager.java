package protocolsupport.injector.pe;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.protocol.Varint21LengthFieldPrepender;
import protocolsupport.protocol.pipeline.common.EncapsulatedConnectionKeepAlive;
import protocolsupport.protocol.pipeline.common.EncapsulatedHandshakeSender;
import protocolsupport.protocol.pipeline.common.PacketCompressor;
import protocolsupport.protocol.pipeline.common.PacketDecompressor;
import protocolsupport.protocol.pipeline.common.VarIntFrameDecoder;
import protocolsupport.utils.netty.ChannelInitializer;

public class PEProxyNetworkManager extends SimpleChannelInboundHandler<ByteBuf> {

	private static final NioEventLoopGroup group = new NioEventLoopGroup();

	private Channel serverconnection;

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		if (serverconnection != null) {
			serverconnection.close();
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf bytebuf) throws Exception {
		ByteBuf cbytebuf = Unpooled.copiedBuffer(bytebuf);
		if (serverconnection == null) {
			serverconnection = connectToServer(ctx.channel(), cbytebuf);
		} else {
			serverconnection.eventLoop().execute(() -> serverconnection.writeAndFlush(cbytebuf));
		}
	}

	private static Channel connectToServer(Channel peclientchannel, ByteBuf loginpacket) {
		InetSocketAddress remote = (InetSocketAddress) peclientchannel.remoteAddress();
		loginpacket.readerIndex(0);
		return new Bootstrap()
		.channel(NioSocketChannel.class)
		.group(group)
		.handler(new ChannelInitializer() {
			@Override
			protected void initChannel(Channel channel) throws Exception {
				channel.pipeline()
				.addLast("ps-encap-hs-sender", new EncapsulatedHandshakeSender(remote, true))
				.addLast("keepalive", new EncapsulatedConnectionKeepAlive())
				.addLast("prepender", new Varint21LengthFieldPrepender())
				.addLast("splitter", new VarIntFrameDecoder())
				.addLast("compress", new PacketCompressor(256))
				.addLast("decompress", new PacketDecompressor())
				.addLast("handler", new SimpleChannelInboundHandler<ByteBuf>() {
					@Override
					public void channelActive(ChannelHandlerContext ctx) throws Exception {
						ctx.writeAndFlush(loginpacket).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
					}
					@Override
					protected void channelRead0(ChannelHandlerContext ctx, ByteBuf bytebuf) throws Exception {
						peclientchannel.writeAndFlush(Unpooled.copiedBuffer(bytebuf)).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
					}
					@Override
					public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
						System.err.println("PE proxy exception occured");
						cause.printStackTrace();
						peclientchannel.close();
					}
					@Override
					public void channelInactive(ChannelHandlerContext ctx) throws Exception {
						super.channelInactive(ctx);
						peclientchannel.close();
					}
				});
			}
		})
		.connect(BungeeCord.getInstance().getConfig().getListeners().iterator().next().getHost())
		.channel();
	}

}
