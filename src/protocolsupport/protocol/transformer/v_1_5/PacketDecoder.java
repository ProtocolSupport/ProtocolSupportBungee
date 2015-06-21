package protocolsupport.protocol.transformer.v_1_5;

import java.util.List;

import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.Protocol;
import protocolsupport.protocol.transformer.v_1_5.reader.PacketReader;
import protocolsupport.utils.ReplayingDecoderBuffer;
import protocolsupport.utils.ReplayingDecoderBuffer.EOFSignal;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketDecoder extends MinecraftDecoder {

	public PacketDecoder(Protocol protocol, boolean server, int protocolVersion) {
		super(protocol, server, protocolVersion);
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
			for (PacketWrapper wrapper : PacketReader.readPacket(packetId, replay)) {
				packets.add(wrapper);
			}
		} catch (EOFSignal e) {
			replay.resetReaderIndex();
		}
	}

}
