package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Login;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.writeable.LegacySingleWriteablePacket;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.typeremapper.LegacyDimension;

public class StartGamePacket extends LegacySingleWriteablePacket<Login> {

	public StartGamePacket() {
		super(LegacyPacketId.Clientbound.PLAY_START_GAME);
	}

	@Override
	protected void write(ByteBuf data, Login packet) {
		data.writeInt(packet.getEntityId());
		StringSerializer.writeShortUTF16BEString(data, packet.getLevelType());
		data.writeByte(packet.getGameMode());
		data.writeByte(LegacyDimension.get(packet.getDimension()));
		data.writeByte(packet.getDifficulty());
		data.writeByte(0);
		data.writeByte(packet.getMaxPlayers());
	}

}
