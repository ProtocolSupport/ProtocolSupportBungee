package protocolsupport.protocol.transformer.v_1_5.packets;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Handshake;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.transformer.TransformedPacket;
import protocolsupport.protocol.transformer.v_1_5.PacketDataSerializer;

public class HandshakePacket extends Handshake implements TransformedPacket {

	private int protocolversion;
	private String username;
	private String host = "";
	private int port;
	private int requestedstate = 2;

	public HandshakePacket() {
		this.protocolversion = ProtocolVersion.MINECRAFT_1_8.getId();
	}

	public HandshakePacket(String username, String host, int port) {
		this.protocolversion = ProtocolVersion.MINECRAFT_1_5_2.getId();
		this.username = username;
		this.host = host;
		this.port = port;
	}

	@Override
	public void read(ByteBuf buf) {
		buf.readByte();
		username = PacketDataSerializer.readString(buf);
		host = PacketDataSerializer.readString(buf);
		port = buf.readInt();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeByte(protocolversion);
		PacketDataSerializer.writeString(username, buf);
		PacketDataSerializer.writeString(host, buf);
		buf.writeInt(port);
	}

	public String getUsername() {
		return username;
	}

	@Override
	public int getProtocolVersion() {
		return this.protocolversion;
	}

	@Override
	public String getHost() {
		return this.host;
	}

	@Override
	public int getPort() {
		return this.port;
	}

	@Override
	public int getRequestedProtocol() {
		return this.requestedstate;
	}

	@Override
	public void setProtocolVersion(int protocolVersion) {
		this.protocolversion = protocolVersion;
	}

	@Override
	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void setRequestedProtocol(int requestedProtocol) {
		this.requestedstate = requestedProtocol;
	}

	@Override
	public int getId() {
		return 0x02;
	}

	@Override
	public boolean shouldWrite() {
		return true;
	}


}
