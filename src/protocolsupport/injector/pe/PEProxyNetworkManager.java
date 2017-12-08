package protocolsupport.injector.pe;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.protocol.Varint21LengthFieldPrepender;
import protocolsupport.protocol.pipeline.common.PacketCompressor;
import protocolsupport.protocol.pipeline.common.PacketDecompressor;
import protocolsupport.protocol.pipeline.common.VarIntFrameDecoder;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.utils.EncapsulatedProtocolUtils;
import protocolsupport.utils.netty.ChannelInitializer;

//TODO: rewrite this
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
				.addLast(new ChannelInboundHandlerAdapter() {
					@Override
					public void channelActive(ChannelHandlerContext ctx) throws Exception {
						channel.pipeline().remove(this);
						ctx.writeAndFlush(EncapsulatedProtocolUtils.createHandshake(remote, true)).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
						super.channelActive(ctx);
					}
				})
				.addLast("idlestatehandler", new IdleStateHandler(0, 5, 0))
				.addLast("keepalive", new ChannelInboundHandlerAdapter() {
					@Override
					public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
						if (evt instanceof IdleStateEvent) {
							if (((IdleStateEvent) evt).state() == IdleState.WRITER_IDLE) {
								ByteBuf rawemptyframedpacket = Unpooled.buffer();
								VarNumberSerializer.writeVarInt(rawemptyframedpacket, 0);
								ctx.writeAndFlush(rawemptyframedpacket);
							}
						}
					}
				})
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
