package protocolsupport.protocol.pipeline.version.legacy.packets;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.EncryptionResponse;
import protocolsupport.protocol.serializer.LegacySerializer;

public class EncryptionResponsePacket extends EncryptionResponse implements TransformedPacket {

	private byte[] sharedSecret;
	private byte[] verifyToken;

	@Override
	public void read(ByteBuf buf) {
		sharedSecret = LegacySerializer.readArray(buf);
		verifyToken = LegacySerializer.readArray(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		LegacySerializer.writeArray(sharedSecret, buf);
		LegacySerializer.writeArray(verifyToken, buf);
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
