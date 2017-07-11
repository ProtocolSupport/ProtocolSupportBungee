package protocolsupport.protocol.pipeline.version.legacy;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.Protocol;
import protocolsupport.LoggerUtil;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.pipeline.version.legacy.reader.LegacyPacketReader;
import protocolsupport.utils.netty.ReplayingDecoderBuffer;
import protocolsupport.utils.netty.ReplayingDecoderBuffer.EOFSignal;

//TODO: split to server -> bungee and client -> bungee packet decoders
public class PacketDecoder extends MinecraftDecoder {

	private final boolean fromclient;
	private final ProtocolVersion version;

	public PacketDecoder(boolean fromclient, ProtocolVersion version) {
		super(Protocol.GAME, fromclient, ProtocolVersion.MINECRAFT_1_7_10.getId());
		this.fromclient = fromclient;
		this.version = version;
	}

	private final ByteBuf internal = Unpooled.buffer();
	private final ReplayingDecoderBuffer replay = new ReplayingDecoderBuffer(internal);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> packets) throws Exception {
		if (!buf.isReadable()) {
			return;
		}
		internal.writeBytes(buf);
		replay.markReaderIndex();
		try {
			while (replay.isReadable()) {
				replay.markReaderIndex();
				int packetId = replay.readByte() & 0xFF;
				for (PacketWrapper wrapper : LegacyPacketReader.readPacket(version, packetId, replay)) {
					if (LoggerUtil.isEnabled()) {
						LoggerUtil.debug((fromclient ? "[From Client] " : "[From Server] ") + "Received packet(id: " + packetId + ", length: " + wrapper.buf.readableBytes() + (wrapper.packet != null ? ", defined data: " + wrapper.packet.toString() : "") + ")");
					}
					packets.add(wrapper);
					internal.discardSomeReadBytes();
				}
			}
		} catch (EOFSignal e) {
			replay.resetReaderIndex();
		}
	}

}
