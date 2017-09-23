package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.protocol.packet.Kick;
import protocolsupport.protocol.packet.middle.WriteableMiddlePacket;
import protocolsupport.protocol.serializer.LegacySerializer;
import protocolsupport.utils.netty.Allocator;

public class KickPacket extends WriteableMiddlePacket<Kick> {

	@Override
	public Collection<ByteBuf> toData(Kick packet) {
		return Collections.singletonList(create(ComponentSerializer.parse(packet.getMessage())[0].toLegacyText()));
	}

	public static ByteBuf create(String message) {
		ByteBuf data = Allocator.allocateBuffer();
		data.writeByte(0xFF);
		LegacySerializer.writeString(data, message);
		return data;
	}

}
