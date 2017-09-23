package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.LoginRequest;
import protocolsupport.protocol.packet.middleimpl.writeable.SingleWriteablePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class LoginRequestServerHandshakePacket extends SingleWriteablePacket<LoginRequest> {

	public LoginRequestServerHandshakePacket() {
		super(0x02);
	}

	@Override
	protected void write(ByteBuf data, LoginRequest packet) {
		data.writeByte(connection.getVersion().getId());
		LegacySerializer.writeString(data, packet.getData());
		LegacySerializer.writeString(data, cache.serverHandshake.getHost());
		data.writeInt(cache.serverHandshake.getPort());
	}

}
