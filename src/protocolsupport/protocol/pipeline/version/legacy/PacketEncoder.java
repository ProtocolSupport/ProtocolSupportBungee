package protocolsupport.protocol.pipeline.version.legacy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.MinecraftEncoder;
import net.md_5.bungee.protocol.Protocol;
import protocolsupport.LoggerUtil;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.pipeline.version.legacy.packets.TransformedPacket;
import protocolsupport.protocol.pipeline.version.legacy.writer.BungeePacketTransformer;
import protocolsupport.protocol.pipeline.version.legacy.writer.ToClientBungeePacketTransformer;
import protocolsupport.protocol.pipeline.version.legacy.writer.ToServerBungeePacketTransformer;
import protocolsupport.utils.Utils;

public class PacketEncoder extends MinecraftEncoder {

	private final ProtocolVersion version;
	private final boolean toclient;
	private final BungeePacketTransformer transformer;

	public PacketEncoder(Protocol protocol, boolean toclient, ProtocolVersion version) {
		super(protocol, toclient, ProtocolVersion.MINECRAFT_1_7_10.getId());
		this.version = version;
		this.toclient = toclient;
		this.transformer = toclient ? new ToClientBungeePacketTransformer() : new ToServerBungeePacketTransformer();
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, DefinedPacket packet, ByteBuf buf) throws Exception {
		TransformedPacket[] packets = null;
		if (packet instanceof TransformedPacket) {
			packets = transformer.transformTransformedPacket(version, (TransformedPacket) packet);
		} else {
			packets = transformer.transformBungeePacket(version, packet);
		}
		for (TransformedPacket tpacket : packets) {
			if (tpacket.shouldWrite()) {
				if (LoggerUtil.isEnabled()) {
					LoggerUtil.debug((toclient ? "[To Client] " : "[To Server] ") + "Sent packet(id: " + tpacket.getId() + ", defined data: " + Utils.toStringAllFields(tpacket) + ")");
				}
				buf.writeByte(tpacket.getId());
				tpacket.write(buf);
			}
		}
	}

}
