package protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets;

import net.md_5.bungee.protocol.packet.Login;
import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.transformer.TransformedPacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.PacketDataSerializer;

public class LoginPacket extends Login implements TransformedPacket {

	private int entityId;
	private short gameMode;
	private int dimension;
	private short difficulty;
	private short maxPlayers;
	private String levelType;

	public LoginPacket() {
	}

	public LoginPacket(int entityId, short gameMode, byte dimension, short difficulty, short maxPlayers, String levelType) {
		this.entityId = entityId;
		this.gameMode = gameMode;
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.maxPlayers = maxPlayers;
		this.levelType = levelType;
	}

	@Override
	public void read(ByteBuf buf) {
		entityId = buf.readInt();
		levelType = PacketDataSerializer.readString(buf);
		gameMode = buf.readByte();
		dimension = buf.readByte();
		difficulty = buf.readByte();
		buf.readByte();
		maxPlayers = buf.readByte();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeInt(entityId);
		PacketDataSerializer.writeString(levelType, buf);
		buf.writeByte(gameMode);
		buf.writeByte(dimension);
		buf.writeByte(difficulty);
		buf.writeByte(0);
		buf.writeByte(maxPlayers);
	}

	@Override
	public int getEntityId() {
		return this.entityId;
	}

	@Override
	public short getGameMode() {
		return this.gameMode;
	}

	@Override
	public int getDimension() {
		return this.dimension;
	}

	@Override
	public short getDifficulty() {
		return this.difficulty;
	}

	@Override
	public short getMaxPlayers() {
		return this.maxPlayers;
	}

	@Override
	public String getLevelType() {
		return this.levelType;
	}

	@Override
	public boolean isReducedDebugInfo() {
		return true;
	}

	@Override
	public boolean shouldWrite() {
		return true;
	}

	@Override
	public int getId() {
		return 0x01;
	}

}
