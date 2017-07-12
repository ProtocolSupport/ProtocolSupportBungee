package protocolsupport.protocol.pipeline.version.legacy.packets;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Respawn;
import protocolsupport.protocol.serializer.LegacySerializer;

public class RespawnPacket extends Respawn implements TransformedPacket {

	private int dimension;
	private short difficulty;
	private short gameMode;
	private String levelType;

	public RespawnPacket() {
	}

	public RespawnPacket(int dimension, short difficulty, short gameMode, String levelType) {
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.gameMode = gameMode;
		this.levelType = levelType;
	}

	@Override
	public void read(final ByteBuf buf) {
		dimension = buf.readInt();
		difficulty = buf.readByte();
		gameMode = buf.readByte();
		buf.readShort();
		levelType = LegacySerializer.readString(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeInt(dimension);
		buf.writeByte(difficulty);
		buf.writeByte(gameMode);
		buf.writeShort(256);
		LegacySerializer.writeString(levelType, buf);
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
	public short getGameMode() {
		return this.gameMode;
	}

	@Override
	public String getLevelType() {
		return this.levelType;
	}

	@Override
	public void setDimension(final int dimension) {
		this.dimension = dimension;
	}

	@Override
	public void setDifficulty(final short difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public void setGameMode(final short gameMode) {
		this.gameMode = gameMode;
	}

	@Override
	public void setLevelType(final String levelType) {
		this.levelType = levelType;
	}

	@Override
	public int getId() {
		return 0x09;
	}

}
