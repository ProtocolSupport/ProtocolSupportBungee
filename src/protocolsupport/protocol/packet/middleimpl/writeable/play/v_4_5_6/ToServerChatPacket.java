package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Chat;
import protocolsupport.protocol.packet.middle.WriteableMiddlePacket;
import protocolsupport.protocol.serializer.LegacySerializer;
import protocolsupport.utils.netty.Allocator;

public class ToServerChatPacket extends WriteableMiddlePacket<Chat> {

	@Override
	public Collection<ByteBuf> toData(Chat packet) {
		if (packet.getPosition() != 2) {
			ByteBuf data = Allocator.allocateBuffer();
			data.writeByte(0x03);
			LegacySerializer.writeString(data, packet.getMessage());
			return Collections.singletonList(data);
		} else {
			return Collections.emptyList();
		}
	}

}
