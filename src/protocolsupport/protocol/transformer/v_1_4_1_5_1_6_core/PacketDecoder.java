package protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core;

import java.util.List;

import net.md_5.bungee.protocol.PacketWrapper;
import protocolsupport.LoggerUtil;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.reader.PacketReader;
import protocolsupport.utils.ReplayingDecoderBuffer;
import protocolsupport.utils.ReplayingDecoderBuffer.EOFSignal;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder {

	private boolean server;
	private ProtocolVersion version;
	public PacketDecoder(boolean server, ProtocolVersion version) {
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
						"Received packet(id: "+packetId + ", length: "+wrapper.buf.readableBytes() + (wrapper.packet != null ? ", defined data: " + wrapper.packet.toString() : "") +
						")"
					);
				}
				packets.add(wrapper);
			}
		} catch (EOFSignal e) {
			replay.resetReaderIndex();
		}
	}

}
