package protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets;

import protocolsupport.protocol.transformer.TransformedPacket;
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
