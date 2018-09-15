package protocolsupport.protocol.pipeline.version.v_pe;

import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;

public class NoopDefinedPacket extends DefinedPacket {

	@Override
	public void handle(AbstractPacketHandler abstractPacketHandler) throws Exception {
	}

	@Override
	public boolean equals(Object o) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		return null;
	}
}
