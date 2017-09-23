package protocolsupport.protocol.packet.middleimpl.readable.play.v_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Team;
import protocolsupport.protocol.packet.middleimpl.readable.DefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class ScoreboardTeamPacket extends DefinedReadableMiddlePacket {

	public static final int PACKET_ID = 0xD1;

	public ScoreboardTeamPacket() {
		super(PACKET_ID);
	}

	protected String name;
	protected byte mode;
	protected String displayName;
	protected String prefix;
	protected String suffix;
	protected byte friendlyFire;
	protected String[] players;

	@Override
	protected void read0(ByteBuf from) {
		this.name = LegacySerializer.readString(from);
		this.mode = from.readByte();
		if ((this.mode == 0) || (this.mode == 2)) {
			this.displayName = LegacySerializer.readString(from);
			this.prefix = LegacySerializer.readString(from);
			this.suffix = LegacySerializer.readString(from);
			this.friendlyFire = from.readByte();
		}
		if ((this.mode == 0) || (this.mode == 3) || (this.mode == 4)) {
			int len = from.readShort();
			this.players = new String[len];
			for (int i = 0; i < len; ++i) {
				this.players[i] = LegacySerializer.readString(from);
			}
		}
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(
			new Team(name, mode, displayName, prefix, suffix, "always", "always", (byte) -1, friendlyFire, players), Unpooled.wrappedBuffer(readbytes)
		));
	}

}
