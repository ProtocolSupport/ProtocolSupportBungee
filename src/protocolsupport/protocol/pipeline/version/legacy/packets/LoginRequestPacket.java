package protocolsupport.protocol.pipeline.version.legacy.packets;

import net.md_5.bungee.protocol.packet.LoginRequest;

public class LoginRequestPacket extends LoginRequest implements TransformedPacket {

	private String username;

	public LoginRequestPacket(String username) {
		this.username = username;
	}

	@Override
	public String getData() {
		return this.username;
	}

	@Override
	public void setData(String data) {
		this.username = data;
	}

	@Override
	public int getId() {
		return -1;
	}

	@Override
	public boolean shouldWrite() {
		return false;
	}

}
