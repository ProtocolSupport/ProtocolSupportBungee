package protocolsupport.protocol.packet.middleimpl.writeable.play.v_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.ScoreboardScore;
import protocolsupport.protocol.packet.middleimpl.writeable.SingleWriteablePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class ScoreboardScorePacket extends SingleWriteablePacket<ScoreboardScore> {

	public ScoreboardScorePacket() {
		super(0xCF);
	}

	@Override
	protected void write(ByteBuf data, ScoreboardScore packet) {
		LegacySerializer.writeString(data, packet.getItemName());
		data.writeByte(packet.getAction());
		if (packet.getAction() != 1) {
			LegacySerializer.writeString(data, packet.getScoreName());
			data.writeInt(packet.getValue());
		}
	}

}
