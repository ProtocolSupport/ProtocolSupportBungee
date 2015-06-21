package protocolsupport.protocol.transformer.v_1_5.packets;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.transformer.TransformedPacket;
import protocolsupport.protocol.transformer.v_1_5.PacketDataSerializer;
import net.md_5.bungee.EncryptionUtil;
import net.md_5.bungee.protocol.packet.EncryptionRequest;

public class EncryptionRequestPacket extends EncryptionRequest implements TransformedPacket {

	private String serverId = "";
	private byte[] publicKey = EncryptionUtil.keys.getPublic().getEncoded();
	private byte[] verifyToken = new byte[] { 1, 2, 3, 4 }; 

	@Override
	public void read(ByteBuf buf) {
		serverId = PacketDataSerializer.readString(buf);
		publicKey = PacketDataSerializer.readArray(buf);
		verifyToken = PacketDataSerializer.readArray(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		PacketDataSerializer.writeString(serverId, buf);
		PacketDataSerializer.writeArray(publicKey, buf);
		PacketDataSerializer.writeArray(verifyToken, buf);
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
