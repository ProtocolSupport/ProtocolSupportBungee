package protocolsupport.protocol.pipeline.version.legacy.packets;

import net.md_5.bungee.protocol.packet.ClientStatus;

public class ClientStatusPacket extends ClientStatus implements TransformedPacket {

	@Override
	public boolean shouldWrite() {
		return getPayload() == 1;
	}

	@Override
	public int getId() {
		return 0xCD;
	}

}
