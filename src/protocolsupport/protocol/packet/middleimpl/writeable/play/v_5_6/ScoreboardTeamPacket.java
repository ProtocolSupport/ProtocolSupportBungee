package protocolsupport.protocol.packet.middleimpl.writeable.play.v_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Team;
import protocolsupport.protocol.packet.middleimpl.writeable.SingleWriteablePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class ScoreboardTeamPacket extends SingleWriteablePacket<Team> {

	public ScoreboardTeamPacket() {
		super(0xD1);
	}

	@Override
	protected void write(ByteBuf data, Team packet) {
		LegacySerializer.writeString(data, packet.getName());
		int mode = packet.getMode();
		data.writeByte(mode);
		if ((mode == 0) || (mode == 2)) {
			LegacySerializer.writeString(data, packet.getDisplayName());
			LegacySerializer.writeString(data, packet.getPrefix());
			LegacySerializer.writeString(data, packet.getSuffix());
			data.writeByte(packet.getFriendlyFire());
		}
		if ((mode == 0) || (mode == 3) || (mode == 4)) {
			data.writeShort(packet.getPlayers().length);
			for (String player : packet.getPlayers()) {
				LegacySerializer.writeString(data, player);
			}
		}
	}

}
