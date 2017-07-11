package protocolsupport.protocol.pipeline.version.legacy.packets;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.EncryptionUtil;
import net.md_5.bungee.protocol.packet.EncryptionRequest;
import protocolsupport.protocol.serializer.LegacySerializer;

public class EncryptionRequestPacket extends EncryptionRequest implements TransformedPacket {

	private String serverId;
	private byte[] publicKey = EncryptionUtil.keys.getPublic().getEncoded();
	private byte[] verifyToken;

	public EncryptionRequestPacket() {
	}

	public EncryptionRequestPacket(String serverId, byte[] verifyToken) {
		this.serverId = serverId;
		this.verifyToken = verifyToken.clone();
	}

	@Override
	public void read(ByteBuf buf) {
		serverId = LegacySerializer.readString(buf);
		publicKey = LegacySerializer.readArray(buf);
		verifyToken = LegacySerializer.readArray(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		LegacySerializer.writeString(serverId, buf);
		LegacySerializer.writeArray(publicKey, buf);
		LegacySerializer.writeArray(verifyToken, buf);
	}

	@Override
	public String getServerId() {
		return this.serverId;
	}

	@Override
	public byte[] getPublicKey() {
		return this.publicKey;
	}

	@Override
	public byte[] getVerifyToken() {
		return this.verifyToken;
	}

	@Override
	public void setServerId(final String serverId) {
		this.serverId = serverId;
	}

	@Override
	public void setPublicKey(final byte[] publicKey) {
		this.publicKey = publicKey;
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
		return 0xFD;
	}

}
