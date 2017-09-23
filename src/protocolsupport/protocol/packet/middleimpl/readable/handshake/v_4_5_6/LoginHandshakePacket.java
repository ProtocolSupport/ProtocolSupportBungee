package protocolsupport.protocol.packet.middleimpl.readable.handshake.v_4_5_6;

import java.util.Arrays;
import java.util.Collection;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Handshake;
import net.md_5.bungee.protocol.packet.LoginRequest;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.packet.middleimpl.readable.DefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class LoginHandshakePacket extends DefinedReadableMiddlePacket {

	public static final int PACKET_ID = 0x02;

	public LoginHandshakePacket() {
		super(PACKET_ID);
	}

	protected String username;
	protected String host;
	protected int port;

	@Override
	protected void read0(ByteBuf from) {
		from.readByte();
		username = LegacySerializer.readString(from);
		host = LegacySerializer.readString(from);
		port = from.readInt();
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Arrays.asList(
			new PacketWrapper(new Handshake(ProtocolVersion.MINECRAFT_1_7_10.getId(), host, port, 2), Unpooled.wrappedBuffer(readbytes)),
			new PacketWrapper(new LoginRequest(username), Unpooled.EMPTY_BUFFER)
		);
	}

}
