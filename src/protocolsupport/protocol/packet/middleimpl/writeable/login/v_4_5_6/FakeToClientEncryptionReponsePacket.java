package protocolsupport.protocol.packet.middleimpl.writeable.login.v_4_5_6;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.packet.middleimpl.readable.login.v_4_5_6.EncryptionResponsePacket.FakeToClientEncrpytionResponse;
import protocolsupport.protocol.packet.middleimpl.writeable.SingleWriteablePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class FakeToClientEncryptionReponsePacket extends SingleWriteablePacket<FakeToClientEncrpytionResponse> {

	public FakeToClientEncryptionReponsePacket() {
		super(0xFC);
	}

	@Override
	protected void write(ByteBuf data, FakeToClientEncrpytionResponse packet) {
		LegacySerializer.writeArray(data, new byte[0]);
		LegacySerializer.writeArray(data, new byte[0]);
	}

}
