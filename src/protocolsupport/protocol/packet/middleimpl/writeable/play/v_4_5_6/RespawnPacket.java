package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Respawn;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.writeable.LegacySingleWriteablePacket;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.typeremapper.LegacyDimension;

public class RespawnPacket extends LegacySingleWriteablePacket<Respawn> {

	public RespawnPacket() {
		super(LegacyPacketId.Clientbound.PLAY_RESPAWN);
	}

	@Override
	protected void write(ByteBuf data, Respawn packet) {
		data.writeInt(LegacyDimension.get(packet.getDimension()));
		data.writeByte(packet.getDifficulty());
		data.writeByte(packet.getGameMode());
		data.writeShort(256);
		StringSerializer.writeShortUTF16BEString(data, packet.getLevelType());
	}

}
