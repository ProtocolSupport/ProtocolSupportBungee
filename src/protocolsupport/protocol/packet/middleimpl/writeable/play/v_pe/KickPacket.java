package protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.protocol.packet.Kick;
import protocolsupport.protocol.packet.middleimpl.writeable.PESingleWriteablePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class KickPacket extends PESingleWriteablePacket<Kick> {

	public KickPacket() {
		super(5);
	}

	@Override
	protected void write(ByteBuf data, Kick packet) {
		data.writeBoolean(false); //do not hide disconnection screen
		StringSerializer.writeVarIntUTF8String(data, ComponentSerializer.parse(packet.getMessage())[0].toLegacyText());
	}

}
