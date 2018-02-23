package protocolsupport.protocol.packet.middleimpl.writeable.login.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.EncryptionRequest;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.writeable.LegacySingleWriteablePacket;
import protocolsupport.protocol.serializer.ArraySerializer;
import protocolsupport.protocol.serializer.StringSerializer;

public class EncryptionRequestPacket extends LegacySingleWriteablePacket<EncryptionRequest> {

	public EncryptionRequestPacket() {
		super(LegacyPacketId.Clientbound.LOGIN_ENCRYPTION_REQUEST);
	}

	@Override
	protected void write(ByteBuf data, EncryptionRequest packet) {
		StringSerializer.writeShortUTF16BEString(data, packet.getServerId());
		ArraySerializer.writeShortLengthByteArray(data, packet.getPublicKey());
		ArraySerializer.writeShortLengthByteArray(data, packet.getVerifyToken());
	}

}
