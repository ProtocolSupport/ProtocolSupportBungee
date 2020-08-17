package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import java.util.Arrays;
import java.util.Collection;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Login;
import net.md_5.bungee.protocol.packet.LoginSuccess;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class StartGamePacket extends LegacyDefinedReadableMiddlePacket {

	public StartGamePacket() {
		super(LegacyPacketId.Clientbound.PLAY_START_GAME);
	}

	protected int entityId;
	protected boolean hardcode;
	protected int gamemode;
	protected int dimension;
	protected int difficulty;
	protected int maxPlayers;
	protected String levelType;

	@Override
	protected void read0(ByteBuf from) {
		entityId = from.readInt();
		levelType = StringSerializer.readShortUTF16BEString(from);
		int	sGamemodeHardcore = from.readByte();
		hardcode = (sGamemodeHardcore & 0b1000) != 0;
		gamemode = (sGamemodeHardcore & 0b111);
		dimension = from.readByte();
		difficulty = from.readByte();
		from.readByte();
		maxPlayers = from.readByte();
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Arrays.asList(
			new PacketWrapper(new LoginSuccess(), Unpooled.EMPTY_BUFFER),
			new PacketWrapper(
				new Login(entityId, hardcode, (short) gamemode, (short) gamemode, null, null, Integer.valueOf(dimension), null, 0, (short) difficulty, (short) maxPlayers, levelType, 10, false, true, false, false),
				Unpooled.wrappedBuffer(readbytes)
			)
		);
	}

}
