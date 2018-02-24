package protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Chat;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_pe.FromClientChatPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.PESingleWriteablePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class ToServerChatPacket extends PESingleWriteablePacket<Chat> {

	public ToServerChatPacket() {
		super(PEPacketId.Dualbound.PLAY_CHAT);
	}

	@Override
	protected void write(ByteBuf data, Chat packet) {
		data.writeByte(FromClientChatPacket.CLIENT_CHAT_TYPE); //type
		data.writeByte(0); //isLocalise?
		StringSerializer.writeVarIntUTF8String(data, ""); //sender username
		StringSerializer.writeVarIntUTF8String(data, packet.getMessage());
		StringSerializer.writeVarIntUTF8String(data, ""); //Xbox user ID
	}

}
