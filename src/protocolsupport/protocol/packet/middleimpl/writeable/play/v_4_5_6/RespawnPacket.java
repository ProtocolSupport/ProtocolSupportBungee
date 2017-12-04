package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Respawn;
import protocolsupport.protocol.packet.middleimpl.writeable.LegacySingleWriteablePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class RespawnPacket extends LegacySingleWriteablePacket<Respawn> {

	public RespawnPacket() {
		super(0x09);
	}

	@Override
	protected void write(ByteBuf data, Respawn packet) {
		data.writeInt(packet.getDimension());
		data.writeByte(packet.getDifficulty());
		data.writeByte(packet.getGameMode());
		data.writeShort(256);
		StringSerializer.writeShortUTF16BEString(data, packet.getLevelType());
	}

}
