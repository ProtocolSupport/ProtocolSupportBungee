package protocolsupport.protocol.packet.middleimpl.readable.play.v_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.ScoreboardScore;
import protocolsupport.protocol.packet.middleimpl.readable.DefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class ScoreboardScorePacket extends DefinedReadableMiddlePacket {

	public static final int PACKET_ID = 0xCF;

	public ScoreboardScorePacket() {
		super(PACKET_ID);
	}

	protected String itemName;
	protected byte action;
	protected String scoreName;
	protected int value;

	@Override
	protected void read0(ByteBuf from) {
		itemName = LegacySerializer.readString(from);
		action = from.readByte();
		if (action != 1) {
			scoreName = LegacySerializer.readString(from);
			value = from.readInt();
		}
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new ScoreboardScore(itemName, action, scoreName, value), Unpooled.wrappedBuffer(readbytes)));
	}

}
