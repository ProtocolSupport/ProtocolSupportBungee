package protocolsupport.protocol.packet.middleimpl.writeable.play.v_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.ScoreboardScore;
import protocolsupport.protocol.packet.middleimpl.writeable.LegacySingleWriteablePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class ScoreboardScorePacket extends LegacySingleWriteablePacket<ScoreboardScore> {

	public ScoreboardScorePacket() {
		super(0xCF);
	}

	@Override
	protected void write(ByteBuf data, ScoreboardScore packet) {
		StringSerializer.writeShortUTF16BEString(data, packet.getItemName());
		data.writeByte(packet.getAction());
		if (packet.getAction() != 1) {
			StringSerializer.writeShortUTF16BEString(data, packet.getScoreName());
			data.writeInt(packet.getValue());
		}
	}

}
