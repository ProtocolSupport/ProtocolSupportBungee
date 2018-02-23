package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.ClientSettings;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.writeable.LegacySingleWriteablePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class ClientSettingsPacket extends LegacySingleWriteablePacket<ClientSettings> {

	public ClientSettingsPacket() {
		super(LegacyPacketId.Serverbound.PLAY_CLIENT_SETTINGS);
	}

	@Override
	protected void write(ByteBuf data, ClientSettings packet) {
		StringSerializer.writeShortUTF16BEString(data, packet.getLocale());
		data.writeByte(packet.getViewDistance());
		data.writeByte(packet.getChatFlags());
		data.writeByte(packet.getDifficulty());
		data.writeBoolean(((packet.getSkinParts() >> 0x01) & 1) == 1);
	}

}
