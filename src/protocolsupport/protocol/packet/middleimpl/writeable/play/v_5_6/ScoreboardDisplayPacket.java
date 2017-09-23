package protocolsupport.protocol.packet.middleimpl.writeable.play.v_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.ScoreboardDisplay;
import protocolsupport.protocol.packet.middleimpl.writeable.SingleWriteablePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class ScoreboardDisplayPacket extends SingleWriteablePacket<ScoreboardDisplay> {

	public ScoreboardDisplayPacket() {
		super(0xD1);
	}

	@Override
	protected void write(ByteBuf data, ScoreboardDisplay packet) {
		data.writeByte(packet.getPosition());
		LegacySerializer.writeString(data, packet.getName());
	}

}
