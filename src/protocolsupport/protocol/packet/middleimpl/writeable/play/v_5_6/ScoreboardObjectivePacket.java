package protocolsupport.protocol.packet.middleimpl.writeable.play.v_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.ScoreboardObjective;
import protocolsupport.protocol.packet.middleimpl.writeable.LegacySingleWriteablePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class ScoreboardObjectivePacket extends LegacySingleWriteablePacket<ScoreboardObjective> {

	public ScoreboardObjectivePacket(int packetId) {
		super(0xCE);
	}

	@Override
	protected void write(ByteBuf data, ScoreboardObjective packet) {
		StringSerializer.writeShortUTF16BEString(data, packet.getName());
		StringSerializer.writeShortUTF16BEString(data, packet.getValue());
		data.writeByte(packet.getAction());
	}

}
