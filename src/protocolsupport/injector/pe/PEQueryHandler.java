package protocolsupport.injector.pe;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToByteEncoder;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.query.QueryHandler;

public class PEQueryHandler extends QueryHandler {

    public static final String NAME = "peproxy-query-read";

    public PEQueryHandler() {
        super(
            BungeeCord.getInstance(),
            BungeeCord.getInstance().getConfig().getListeners().iterator().next()
        );
    }

    private boolean hasChecked = false;
    private boolean isQuery = false;

    public Writer createWriter() {
        return new Writer();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (hasChecked && !isQuery) {
            ctx.fireChannelRead(msg);
            return;
        }
        if (msg instanceof ByteBuf) {
            final Channel channel = ctx.channel();
            final ByteBuf buf = (ByteBuf)msg;
            final ByteBuf peakBuf = buf.duplicate();
            hasChecked = true; //we check the first packet only
            if ((peakBuf.readUnsignedByte() != 0xFE) || (peakBuf.readUnsignedByte() != 0xFD)) {
                ctx.fireChannelRead(msg);
                return;
            }
            //TODO: queries just clog up a pipeline until timeout. we need to correctly close these...
            channel.eventLoop().schedule(() -> {
                channel.close();
            }, 500, TimeUnit.MILLISECONDS);
            isQuery = true;
            DatagramPacket gram = new DatagramPacket(buf,
                    (InetSocketAddress)ctx.channel().localAddress(),
                    (InetSocketAddress)ctx.channel().remoteAddress()
            );
            super.channelRead(ctx, gram);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    public class Writer extends MessageToByteEncoder<DatagramPacket> {
        public static final String NAME = "peproxy-query-write";

        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, ByteBuf byteBuf) throws Exception {
            if(!isQuery) {
                channelHandlerContext.fireChannelRead(datagramPacket);
                return;
            }
            byteBuf.writeBytes(datagramPacket.content());
        }
    }
}
