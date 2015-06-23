package protocolsupport.protocol.transformer.v_1_5_v1_6_shared;

import java.util.List;

import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.Protocol;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.reader.PacketReader;
import protocolsupport.utils.ReplayingDecoderBuffer;
import protocolsupport.utils.ReplayingDecoderBuffer.EOFSignal;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketDecoder extends MinecraftDecoder {

	private ProtocolVersion version;
	public PacketDecoder(Protocol protocol, boolean server, int protocolVersion, ProtocolVersion version) {
		super(protocol, server, protocolVersion);
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
			for (PacketWrapper wrapper : PacketReader.readPacket(version, replay.readByte() & 0xFF, replay)) {
				packets.add(wrapper);
			}
		} catch (EOFSignal e) {
			replay.resetReaderIndex();
		}
	}

}
