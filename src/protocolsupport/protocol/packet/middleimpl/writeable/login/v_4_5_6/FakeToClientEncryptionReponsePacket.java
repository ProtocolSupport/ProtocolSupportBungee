package protocolsupport.protocol.packet.middleimpl.writeable.login.v_4_5_6;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.packet.middleimpl.readable.login.v_4_5_6.EncryptionResponsePacket.FakeToClientEncrpytionResponse;
import protocolsupport.protocol.packet.middleimpl.writeable.LegacySingleWriteablePacket;
import protocolsupport.protocol.serializer.ArraySerializer;

public class FakeToClientEncryptionReponsePacket extends LegacySingleWriteablePacket<FakeToClientEncrpytionResponse> {

	public FakeToClientEncryptionReponsePacket() {
		super(0xFC);
	}

	@Override
	protected void write(ByteBuf data, FakeToClientEncrpytionResponse packet) {
		ArraySerializer.writeShortLengthByteArray(data, new byte[0]);
		ArraySerializer.writeShortLengthByteArray(data, new byte[0]);
	}

}
