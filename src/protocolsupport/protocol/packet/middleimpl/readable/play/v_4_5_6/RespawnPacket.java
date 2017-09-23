package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Respawn;
import protocolsupport.protocol.packet.middleimpl.readable.DefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class RespawnPacket extends DefinedReadableMiddlePacket {

	public static final int PACKET_ID = 0x09;

	public RespawnPacket() {
		super(PACKET_ID);
	}

	protected int dimension;
	protected int difficulty;
	protected int gamemode;
	protected String levelType;

	@Override
	protected void read0(ByteBuf from) {
		dimension = from.readInt();
		difficulty = from.readByte();
		gamemode = from.readByte();
		from.readShort();
		levelType = LegacySerializer.readString(from);
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new Respawn(dimension, (short) difficulty, (short) gamemode, levelType), Unpooled.wrappedBuffer(readbytes)));
	}

}
