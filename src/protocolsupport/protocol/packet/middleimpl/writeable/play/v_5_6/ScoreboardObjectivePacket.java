package protocolsupport.protocol.packet.middleimpl.writeable.play.v_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.ScoreboardObjective;
import protocolsupport.protocol.packet.middleimpl.writeable.SingleWriteablePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class ScoreboardObjectivePacket extends SingleWriteablePacket<ScoreboardObjective> {

	public ScoreboardObjectivePacket(int packetId) {
		super(0xCE);
	}

	@Override
	protected void write(ByteBuf data, ScoreboardObjective packet) {
		LegacySerializer.writeString(data, packet.getName());
		LegacySerializer.writeString(data, packet.getValue());
		data.writeByte(packet.getAction());
	}

}
