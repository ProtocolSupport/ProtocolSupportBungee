package protocolsupport.protocol.packet.middleimpl.readable.play.v_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.ScoreboardDisplay;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class ScoreboardDisplayPacket extends LegacyDefinedReadableMiddlePacket {

	public ScoreboardDisplayPacket() {
		super(LegacyPacketId.Clientbound.PLAY_SCOREBOARD_DISPLAY_SLOT);
	}

	protected byte position;
	protected String name;

	@Override
	protected void read0(ByteBuf from) {
		position = from.readByte();
		name = StringSerializer.readShortUTF16BEString(from);
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new ScoreboardDisplay(position, name), Unpooled.wrappedBuffer(readbytes)));
	}

}
