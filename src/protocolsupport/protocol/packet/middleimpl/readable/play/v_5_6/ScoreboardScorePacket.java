package protocolsupport.protocol.packet.middleimpl.readable.play.v_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.ScoreboardScore;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class ScoreboardScorePacket extends LegacyDefinedReadableMiddlePacket {

	public ScoreboardScorePacket() {
		super(LegacyPacketId.Clientbound.PLAY_SCOREBOARD_SCORE);
	}

	protected String itemName;
	protected byte action;
	protected String scoreName;
	protected int value;

	@Override
	protected void read0(ByteBuf from) {
		itemName = StringSerializer.readShortUTF16BEString(from);
		action = from.readByte();
		if (action != 1) {
			scoreName = StringSerializer.readShortUTF16BEString(from);
			value = from.readInt();
		}
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new ScoreboardScore(itemName, action, scoreName, value), Unpooled.wrappedBuffer(readbytes)));
	}

}
