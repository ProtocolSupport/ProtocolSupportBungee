package protocolsupport.protocol.pipeline.version.legacy.packets;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Team;
import protocolsupport.protocol.serializer.LegacySerializer;

public class TeamPacket extends Team implements TransformedPacket {

	private String name;
	private byte mode;
	private String displayName;
	private String prefix;
	private String suffix;
	private byte friendlyFire;
	private String[] players;

	public TeamPacket() {
	}

	public TeamPacket(String name) {
		this.name = name;
		this.mode = 1;
	}

	public TeamPacket(String name, byte mode, String displayName, String prefix, String suffix, byte friendlyFire, String[] players) {
		this.name = name;
		this.mode = mode;
		this.displayName = displayName;
		this.prefix = prefix;
		this.suffix = suffix;
		this.friendlyFire = friendlyFire;
		this.players = players;
	}

	@Override
	public void read(ByteBuf buf) {
		this.name = LegacySerializer.readString(buf);
		this.mode = buf.readByte();
		if ((this.mode == 0) || (this.mode == 2)) {
			this.displayName = LegacySerializer.readString(buf);
			this.prefix = LegacySerializer.readString(buf);
			this.suffix = LegacySerializer.readString(buf);
			this.friendlyFire = buf.readByte();
		}
		if ((this.mode == 0) || (this.mode == 3) || (this.mode == 4)) {
			int len = buf.readShort();
			this.players = new String[len];
			for (int i = 0; i < len; ++i) {
				this.players[i] = LegacySerializer.readString(buf);
			}
		}
	}

	@Override
	public void write(ByteBuf buf) {
		LegacySerializer.writeString(this.name, buf);
		buf.writeByte(this.mode);
		if ((this.mode == 0) || (this.mode == 2)) {
			LegacySerializer.writeString(this.displayName, buf);
			LegacySerializer.writeString(this.prefix, buf);
			LegacySerializer.writeString(this.suffix, buf);
			buf.writeByte(this.friendlyFire);
		}
		if ((this.mode == 0) || (this.mode == 3) || (this.mode == 4)) {
			buf.writeShort(this.players.length);
			for (String player : this.players) {
				LegacySerializer.writeString(player, buf);
			}
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public byte getMode() {
		return this.mode;
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public String getPrefix() {
		return this.prefix;
	}

	@Override
	public String getSuffix() {
		return this.suffix;
	}

	@Override
	public byte getFriendlyFire() {
		return this.friendlyFire;
	}

	@Override
	public String[] getPlayers() {
		return this.players;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public void setMode(final byte mode) {
		this.mode = mode;
	}

	@Override
	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	@Override
	public void setPrefix(final String prefix) {
		this.prefix = prefix;
	}

	@Override
	public void setSuffix(final String suffix) {
		this.suffix = suffix;
	}

	@Override
	public void setFriendlyFire(final byte friendlyFire) {
		this.friendlyFire = friendlyFire;
	}

	@Override
	public void setPlayers(final String[] players) {
		this.players = players;
	}

	@Override
	public int getId() {
		return 0xD1;
	}

}
