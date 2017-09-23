package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.protocol.packet.Chat;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.packet.middle.WriteableMiddlePacket;
import protocolsupport.protocol.serializer.LegacySerializer;
import protocolsupport.utils.netty.Allocator;

public class ToClientChatPacket extends WriteableMiddlePacket<Chat> {

	@Override
	public Collection<ByteBuf> toData(Chat packet) {
		if (packet.getPosition() != 2) {
			ByteBuf data = Allocator.allocateBuffer();
			data.writeByte(0x03);
			String message = ComponentSerializer.parse(packet.getMessage())[0].toLegacyText();
			if (connection.getVersion().isBefore(ProtocolVersion.MINECRAFT_1_6_1)) {
				LegacySerializer.writeString(data, message);
			} else {
				LegacySerializer.writeString(data, "{\"text\": \"" + message + "\"}");
			}
			return Collections.singletonList(data);
		} else {
			return Collections.emptyList();
		}
	}

}
