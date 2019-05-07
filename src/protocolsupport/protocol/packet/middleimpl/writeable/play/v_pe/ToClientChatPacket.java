package protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.protocol.packet.Chat;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middleimpl.writeable.PESingleWriteablePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class ToClientChatPacket extends PESingleWriteablePacket<Chat> {

	public ToClientChatPacket() {
		super(PEPacketId.Dualbound.PLAY_CHAT);
	}

	@Override
	protected void write(ByteBuf data, Chat packet) {
		data.writeByte(packet.getPosition() == 2 ? 5 : 0); //type
		data.writeBoolean(false); //isLocalise?
		StringSerializer.writeVarIntUTF8String(data, ComponentSerializer.parse(packet.getMessage())[0].toLegacyText());
		StringSerializer.writeVarIntUTF8String(data, ""); //Xbox user ID
		StringSerializer.writeVarIntUTF8String(data, ""); //Platform Chat ID
	}

}
