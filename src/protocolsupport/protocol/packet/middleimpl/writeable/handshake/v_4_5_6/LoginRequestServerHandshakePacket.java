package protocolsupport.protocol.packet.middleimpl.writeable.handshake.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.LoginRequest;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.writeable.LegacySingleWriteablePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class LoginRequestServerHandshakePacket extends LegacySingleWriteablePacket<LoginRequest> {

	public LoginRequestServerHandshakePacket() {
		super(LegacyPacketId.Serverbound.HANDSHAKE_LOGIN);
	}

	@Override
	protected void write(ByteBuf data, LoginRequest packet) {
		data.writeByte(connection.getVersion().getId());
		StringSerializer.writeShortUTF16BEString(data, packet.getData());
		StringSerializer.writeShortUTF16BEString(data, cache.getServerHandshake().getHost());
		data.writeInt(cache.getServerHandshake().getPort());
	}

}
