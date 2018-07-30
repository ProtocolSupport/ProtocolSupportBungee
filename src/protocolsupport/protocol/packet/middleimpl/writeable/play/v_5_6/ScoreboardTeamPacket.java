package protocolsupport.protocol.packet.middleimpl.writeable.play.v_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Team;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.writeable.LegacySingleWriteablePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class ScoreboardTeamPacket extends LegacySingleWriteablePacket<Team> {

	public ScoreboardTeamPacket() {
		super(LegacyPacketId.Clientbound.PLAY_SCOREBOARD_TEAM);
	}

	@Override
	protected void write(ByteBuf data, Team packet) {
		StringSerializer.writeShortUTF16BEString(data, packet.getName());
		int mode = packet.getMode();
		data.writeByte(mode);
		if ((mode == 0) || (mode == 2)) {
			StringSerializer.writeShortUTF16BEString(data, packet.getDisplayName());
			StringSerializer.writeShortUTF16BEString(data, packet.getPrefix());
			StringSerializer.writeShortUTF16BEString(data, packet.getSuffix());
			data.writeByte(packet.getFriendlyFire());
		}
		if ((mode == 0) || (mode == 3) || (mode == 4)) {
			data.writeShort(packet.getPlayers().length);
			for (String player : packet.getPlayers()) {
				StringSerializer.writeShortUTF16BEString(data, player);
			}
		}
	}

}
