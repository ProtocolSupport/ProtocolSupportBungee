package protocolsupport.protocol.pipeline.version.legacy.packets;

import net.md_5.bungee.protocol.packet.LoginSuccess;

public class LoginSuccessPacket extends LoginSuccess implements TransformedPacket {

	@Override
	public int getId() {
		return -1;
	}

}
