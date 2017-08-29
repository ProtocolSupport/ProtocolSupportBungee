package protocolsupport.protocol.packet.middle;

import java.util.Collection;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.DefinedPacket;

public abstract class WriteableMiddlePacket<T extends DefinedPacket> extends MiddlePacket {

	public abstract Collection<ByteBuf> toData(T packet);

}
