package protocolsupport.protocol.pipeline.version.legacy.packets;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Handshake;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.serializer.LegacySerializer;

public class HandshakePacket extends Handshake implements TransformedPacket {

	private int protocolversion;
	private String username;
	private String host = "";
	private int port;
	private int requestedstate = 2;

	public HandshakePacket() {
	}

	public HandshakePacket(int protocolversion, String username, String host, int port) {
		this.protocolversion = protocolversion;
		this.username = username;
		this.host = host;
		this.port = port;
	}

	@Override
	public void read(ByteBuf buf) {
		protocolversion = buf.readByte();
		username = LegacySerializer.readString(buf);
		host = LegacySerializer.readString(buf);
		port = buf.readInt();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeByte(protocolversion);
		LegacySerializer.writeString(username, buf);
		LegacySerializer.writeString(host, buf);
		buf.writeInt(port);
	}

	public String getUsername() {
		return username;
	}

	@Override
	public int getProtocolVersion() {
		return ProtocolVersion.MINECRAFT_1_7_10.getId();
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

}
