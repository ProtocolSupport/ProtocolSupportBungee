package protocolsupport.protocol.packet.middleimpl.writeable;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.DefinedPacket;
import protocolsupport.protocol.packet.middle.WriteableMiddlePacket;

public class NoopWriteablePacket extends WriteableMiddlePacket<DefinedPacket> {

	@Override
	public Collection<ByteBuf> toData(DefinedPacket packet) {
		return Collections.emptyList();
	}

}
