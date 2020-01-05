package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Respawn;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class RespawnPacket extends LegacyDefinedReadableMiddlePacket {

	public RespawnPacket() {
		super(LegacyPacketId.Clientbound.PLAY_RESPAWN);
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
		levelType = StringSerializer.readShortUTF16BEString(from);
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new Respawn(dimension, 0, (short) difficulty, (short) gamemode, levelType), Unpooled.wrappedBuffer(readbytes)));
	}

}
