package protocolsupport.protocol.pipeline.version.v_1_5;

import net.md_5.bungee.protocol.Protocol;
import protocolsupport.api.Connection;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.FromServerChatPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.KeepAlivePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.KickPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.PlayerListItemPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.PluginMessagePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.RespawnPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.StartGamePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.TabCompleteResponsePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_5_6.ScoreboardDisplayPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_5_6.ScoreboardObjectivePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_5_6.ScoreboardScorePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_5_6.ScoreboardTeamPacket;
import protocolsupport.protocol.pipeline.version.LegacyAbstractFromServerPacketDecoder;
import protocolsupport.protocol.storage.NetworkDataCache;

public class FromServerPacketDecoder extends LegacyAbstractFromServerPacketDecoder {

	{
		registry.register(Protocol.GAME, LegacyPacketId.Dualbound.PLAY_KEEP_ALIVE, KeepAlivePacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Dualbound.PLAY_KICK, KickPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Clientbound.PLAY_START_GAME, StartGamePacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Dualbound.PLAY_CHAT, FromServerChatPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Clientbound.PLAY_RESPAWN, RespawnPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Clientbound.PLAY_PLAYER_LIST, PlayerListItemPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Dualbound.PLAY_TAB_COMPLETE, TabCompleteResponsePacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Clientbound.PLAY_SCOREBOARD_OBJECTIVE, ScoreboardObjectivePacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Clientbound.PLAY_SCOREBOARD_SCORE, ScoreboardScorePacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Clientbound.PLAY_SCOREBOARD_DISPLAY_SLOT, ScoreboardDisplayPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Clientbound.PLAY_SCOREBOARD_TEAM, ScoreboardTeamPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Dualbound.PLAY_PLUGIN_MESSAGE, PluginMessagePacket.class);
	}

	public FromServerPacketDecoder(Connection connection, NetworkDataCache cache) {
		super(connection, cache);
	}

}
