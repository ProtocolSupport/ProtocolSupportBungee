package protocolsupport.protocol.transformer.v_1_5_v1_6_shared;

import java.io.IOException;

import protocolsupport.protocol.transformer.TransformedPacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.reader.BungeePacketTransformer;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.MinecraftEncoder;
import net.md_5.bungee.protocol.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketEncoder extends MinecraftEncoder {

	public PacketEncoder(Protocol protocol, boolean server, int protocolVersion) {
		super(protocol, server, protocolVersion);
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, DefinedPacket packet, ByteBuf buf) throws Exception {
		TransformedPacket[] packets = null;
		//Bungee can send packets too, so we will have to transform them first
		if (!(packet instanceof TransformedPacket)) {
			packets = BungeePacketTransformer.transformBungeePacket(ctx.channel(), packet, buf);
			if (packets == null) {
				throw new IOException("Unable to transform bungee packet "+packet.getClass().getName());
			}
		} else {
			packets = new TransformedPacket[] { (TransformedPacket) packet };
		}
		encodePackets(packets, buf);
	}

	private void encodePackets(TransformedPacket[] packets, ByteBuf buf) {
		for (TransformedPacket tpacket : packets) {
			if (tpacket.shouldWrite()) {
				buf.writeByte(tpacket.getId());
				tpacket.write(buf);
			}
		}
	}

}
