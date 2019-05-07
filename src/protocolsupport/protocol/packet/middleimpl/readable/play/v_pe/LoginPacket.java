package protocolsupport.protocol.packet.middleimpl.readable.play.v_pe;

import java.util.Arrays;
import java.util.Collection;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Login;
import net.md_5.bungee.protocol.packet.LoginSuccess;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.PEDefinedReadableMiddlePacket;
import protocolsupport.protocol.pipeline.version.v_pe.NoopDefinedPacket;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.PEPacketIdSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.utils.netty.Allocator;

public class LoginPacket extends PEDefinedReadableMiddlePacket {

	public LoginPacket() {
		super(PEPacketId.Clientbound.PLAY_START_GAME);
	}

	protected int entityId;
	protected byte gamemode;
	protected int dimension;
	protected int difficulty;

	@Override
	protected void read0(ByteBuf from) {
		VarNumberSerializer.readSVarLong(from); //entity id (but it's actually signed varlong, so we use the field below, which is unsigned)
		entityId = (int) VarNumberSerializer.readVarLong(from);
		gamemode = (byte) VarNumberSerializer.readSVarInt(from);
		float x = from.readFloatLE(); //x
		float y = from.readFloatLE(); //y
		float z = from.readFloatLE(); //z
		from.readFloatLE(); //yaw
		from.readFloatLE(); //pitch
		VarNumberSerializer.readSVarInt(from); //seed
		dimension = RespawnPacket.getPcDimensionId(VarNumberSerializer.readSVarInt(from));
		VarNumberSerializer.readSVarInt(from); //world type (1 - infinite)
		VarNumberSerializer.readSVarInt(from); // world gamemode (SURVIVAL)
		difficulty = VarNumberSerializer.readSVarInt(from);
		cache.setSpawnLocation(x, y, z);
		cache.setStartGameData(MiscSerializer.readAllBytes(from)); //cary this stuff over
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		ByteBuf gameModeSerializer = Allocator.allocateBuffer();
		PEPacketIdSerializer.writePacketId(gameModeSerializer, PEPacketId.Clientbound.PLAY_PLAYER_GAME_TYPE);
		VarNumberSerializer.writeSVarInt(gameModeSerializer, gamemode);
		return Arrays.asList(
			new PacketWrapper(new LoginSuccess(), Unpooled.EMPTY_BUFFER),
			new PacketWrapper(new Login(entityId, gamemode, dimension, (short) difficulty, (short) 1, "", 10, false), Unpooled.EMPTY_BUFFER),
			new PacketWrapper(new NoopDefinedPacket(), gameModeSerializer) //send game mode independently
		);
	}

}
