package protocolsupport.protocol.packet.middleimpl.writeable.play.v_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Kick;
import protocolsupport.protocol.packet.middle.WriteableMiddlePacket;
import protocolsupport.protocol.serializer.LegacySerializer;
import protocolsupport.utils.netty.Allocator;

public class KickPacket extends WriteableMiddlePacket<Kick> {

	@Override
	public Collection<ByteBuf> toData(Kick packet) {
		return Collections.singletonList(create(packet.getMessage()));
	}

	public static ByteBuf create(String message) {
		ByteBuf data = Allocator.allocateBuffer();
		data.writeByte(0xFF);
		LegacySerializer.writeString(data, message);
		return data;
	}

}
