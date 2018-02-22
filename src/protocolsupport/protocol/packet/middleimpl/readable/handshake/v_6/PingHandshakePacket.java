package protocolsupport.protocol.packet.middleimpl.readable.handshake.v_6;

import java.util.Arrays;
import java.util.Collection;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Handshake;
import net.md_5.bungee.protocol.packet.StatusRequest;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.utils.ProtocolVersionsHelper;

public class PingHandshakePacket extends LegacyDefinedReadableMiddlePacket {

	public static final int PACKET_ID = 0xFE;

	public PingHandshakePacket() {
		super(PACKET_ID);
	}

	protected String host;
	protected int port;

	@Override
	protected void read0(ByteBuf from) {
		from.readUnsignedByte();
		from.readUnsignedByte();
		StringSerializer.readShortUTF16BEString(from);
		from.readUnsignedShort();
		from.readUnsignedByte();
		host = StringSerializer.readShortUTF16BEString(from);
		port = from.readInt();
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Arrays.asList(
			new PacketWrapper(new Handshake(ProtocolVersionsHelper.LATEST_PC.getId(), host, port, 1), Unpooled.EMPTY_BUFFER),
			new PacketWrapper(new StatusRequest(), Unpooled.wrappedBuffer(readbytes))
		);
	}

}
