package protocolsupport.protocol.packet.middleimpl.readable.play.v_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.ScoreboardObjective;
import protocolsupport.protocol.packet.middleimpl.readable.DefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class ScoreboardObjectivePacket extends DefinedReadableMiddlePacket {

	public static final int PACKET_ID = 0xCE;

	public ScoreboardObjectivePacket() {
		super(PACKET_ID);
	}

	protected String name;
	protected String value;
	protected byte action;

	@Override
	protected void read0(ByteBuf from) {
		name = LegacySerializer.readString(from);
		value = LegacySerializer.readString(from);
		action = from.readByte();
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new ScoreboardObjective(name, value, "integer", action), Unpooled.wrappedBuffer(readbytes)));
	}

}
