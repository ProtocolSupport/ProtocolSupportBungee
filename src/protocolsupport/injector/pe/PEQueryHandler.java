package protocolsupport.injector.pe;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToByteEncoder;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.query.QueryHandler;

import java.net.InetSocketAddress;

public class PEQueryHandler extends QueryHandler {
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
            ByteBuf buf = (ByteBuf)msg;
            ByteBuf peakBuf = buf.duplicate();

            hasChecked = true;
            if (peakBuf.readUnsignedByte() != 0xFE || peakBuf.readUnsignedByte() != 0xFD) {
                ctx.fireChannelRead(msg);
                return;
            }
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
