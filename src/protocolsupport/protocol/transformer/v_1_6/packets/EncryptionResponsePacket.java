package protocolsupport.protocol.transformer.v_1_6.packets;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.transformer.TransformedPacket;
import protocolsupport.protocol.transformer.v_1_5.PacketDataSerializer;
import net.md_5.bungee.protocol.packet.EncryptionResponse;

public class EncryptionResponsePacket extends EncryptionResponse implements TransformedPacket {

	private byte[] sharedSecret;
	private byte[] verifyToken;

	@Override
	public void read(ByteBuf buf) {
		sharedSecret = PacketDataSerializer.readArray(buf);
		verifyToken = PacketDataSerializer.readArray(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		PacketDataSerializer.writeArray(sharedSecret, buf);
		PacketDataSerializer.writeArray(verifyToken, buf);
	}

	@Override
	public byte[] getSharedSecret() {
		return this.sharedSecret;
	}

	@Override
	public byte[] getVerifyToken() {
		return this.verifyToken;
	}

	@Override
	public void setSharedSecret(final byte[] sharedSecret) {
		this.sharedSecret = sharedSecret;
	}

	@Override
	public void setVerifyToken(final byte[] verifyToken) {
		this.verifyToken = verifyToken;
	}

	@Override
	public boolean shouldWrite() {
		return true;
	}

	@Override
	public int getId() {
		return 0xFC;
	}

}
