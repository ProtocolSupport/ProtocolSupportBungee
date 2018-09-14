package protocolsupport.protocol.packet.middleimpl.readable.play.v_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.ScoreboardObjective;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class ScoreboardObjectivePacket extends LegacyDefinedReadableMiddlePacket {

	public ScoreboardObjectivePacket() {
		super(LegacyPacketId.Clientbound.PLAY_SCOREBOARD_OBJECTIVE);
	}

	protected String name;
	protected String value;
	protected byte action;

	@Override
	protected void read0(ByteBuf from) {
		name = StringSerializer.readShortUTF16BEString(from);
		value = StringSerializer.readShortUTF16BEString(from);
		action = from.readByte();
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new ScoreboardObjective(name, value, ScoreboardObjective.HealthDisplay.INTEGER, action), Unpooled.wrappedBuffer(readbytes)));
	}

}
