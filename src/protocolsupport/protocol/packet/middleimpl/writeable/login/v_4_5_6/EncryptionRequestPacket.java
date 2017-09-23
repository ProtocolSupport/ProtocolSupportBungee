package protocolsupport.protocol.packet.middleimpl.writeable.login.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.EncryptionRequest;
import protocolsupport.protocol.packet.middleimpl.writeable.SingleWriteablePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class EncryptionRequestPacket extends SingleWriteablePacket<EncryptionRequest> {

	public EncryptionRequestPacket() {
		super(0xFD);
	}

	@Override
	protected void write(ByteBuf data, EncryptionRequest packet) {
		LegacySerializer.writeString(data, packet.getServerId());
		LegacySerializer.writeArray(data, packet.getPublicKey());
		LegacySerializer.writeArray(data, packet.getVerifyToken());
	}

}
