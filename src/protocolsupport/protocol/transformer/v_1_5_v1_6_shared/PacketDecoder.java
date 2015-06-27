package protocolsupport.protocol.transformer.v_1_5_v1_6_shared;

import java.util.List;

import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.Protocol;
import protocolsupport.LoggerUtil;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.reader.PacketReader;
import protocolsupport.utils.ReplayingDecoderBuffer;
import protocolsupport.utils.ReplayingDecoderBuffer.EOFSignal;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketDecoder extends MinecraftDecoder {

	private boolean server;
	private ProtocolVersion version;
	public PacketDecoder(Protocol protocol, boolean server, int protocolVersion, ProtocolVersion version) {
		super(protocol, server, protocolVersion);
		this.server = server;
		this.version = version;
	}

	private ReplayingDecoderBuffer replay = new ReplayingDecoderBuffer();

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> packets) throws Exception {
		if (!buf.isReadable()) {
			return;
		}
		replay.setCumulation(buf);
		replay.markReaderIndex();
		try {
			int packetId = replay.readByte() & 0xFF;
			for (PacketWrapper wrapper : PacketReader.readPacket(version, packetId, replay)) {
				if (LoggerUtil.isEnabled()) {
					LoggerUtil.debug(
						(server ? "[From Client] " : "[From Server] ") +
						"Received packet(id: "+packetId + ", length: "+wrapper.buf.readableBytes() + (wrapper.packet != null ? ", defined data: " + wrapper.packet.toString() : "")
					);
				}
				packets.add(wrapper);
			}
		} catch (EOFSignal e) {
			replay.resetReaderIndex();
		}
	}

}
