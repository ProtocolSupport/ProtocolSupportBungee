package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.LoginRequest;
import protocolsupport.protocol.packet.middleimpl.writeable.LegacySingleWriteablePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class LoginRequestServerHandshakePacket extends LegacySingleWriteablePacket<LoginRequest> {

	public LoginRequestServerHandshakePacket() {
		super(0x02);
	}

	@Override
	protected void write(ByteBuf data, LoginRequest packet) {
		data.writeByte(connection.getVersion().getId());
		StringSerializer.writeShortUTF16BEString(data, packet.getData());
		StringSerializer.writeShortUTF16BEString(data, cache.serverHandshake.getHost());
		data.writeInt(cache.serverHandshake.getPort());
	}

}
