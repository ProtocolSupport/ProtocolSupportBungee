package protocolsupport.protocol.packet.middle;

import java.util.Collection;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketWrapper;
import protocolsupport.utils.Utils;

public abstract class ReadableMiddlePacket extends MiddlePacket {

	public abstract void read(ByteBuf data);

	public abstract Collection<PacketWrapper> toNative();

	@Override
	public String toString() {
		return Utils.toStringAllFields(this);
	}

}
