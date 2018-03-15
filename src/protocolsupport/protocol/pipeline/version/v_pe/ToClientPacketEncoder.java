package protocolsupport.protocol.pipeline.version.v_pe;

import net.md_5.bungee.protocol.packet.BossBar;
import net.md_5.bungee.protocol.packet.Chat;
import net.md_5.bungee.protocol.packet.EncryptionRequest;
import net.md_5.bungee.protocol.packet.KeepAlive;
import net.md_5.bungee.protocol.packet.Kick;
import net.md_5.bungee.protocol.packet.Login;
import net.md_5.bungee.protocol.packet.LoginSuccess;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import net.md_5.bungee.protocol.packet.PluginMessage;
import net.md_5.bungee.protocol.packet.Respawn;
import net.md_5.bungee.protocol.packet.ScoreboardDisplay;
import net.md_5.bungee.protocol.packet.ScoreboardObjective;
import net.md_5.bungee.protocol.packet.ScoreboardScore;
import net.md_5.bungee.protocol.packet.StatusResponse;
import net.md_5.bungee.protocol.packet.TabCompleteResponse;
import net.md_5.bungee.protocol.packet.Team;
import net.md_5.bungee.protocol.packet.Title;
import protocolsupport.api.Connection;
import protocolsupport.protocol.packet.middleimpl.writeable.NoopWriteablePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.login.v_pe.LoginSuccessPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe.KickPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe.PlayerListItemPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe.RespawnPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe.StartGamePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe.ToClientChatPacket;
import protocolsupport.protocol.pipeline.version.AbstractPacketEncoder;
import protocolsupport.protocol.storage.NetworkDataCache;

public class ToClientPacketEncoder extends AbstractPacketEncoder {

	{
		registry.register(EncryptionRequest.class, NoopWriteablePacket.class);
		registry.register(LoginSuccess.class, LoginSuccessPacket.class);
		registry.register(Login.class, StartGamePacket.class);
		registry.register(StatusResponse.class, NoopWriteablePacket.class);
		registry.register(Kick.class, KickPacket.class);
		registry.register(KeepAlive.class, NoopWriteablePacket.class);
		registry.register(PluginMessage.class, NoopWriteablePacket.class);
		registry.register(Respawn.class, RespawnPacket.class);
		registry.register(Chat.class, ToClientChatPacket.class);
		registry.register(ScoreboardDisplay.class, NoopWriteablePacket.class);
		registry.register(ScoreboardObjective.class, NoopWriteablePacket.class);
		registry.register(ScoreboardScore.class, NoopWriteablePacket.class);
		registry.register(Team.class, NoopWriteablePacket.class);
		registry.register(PlayerListItem.class, PlayerListItemPacket.class);
		registry.register(TabCompleteResponse.class, NoopWriteablePacket.class);
		registry.register(BossBar.class, NoopWriteablePacket.class);
		registry.register(Title.class, NoopWriteablePacket.class);
	}

	public ToClientPacketEncoder(Connection connection, NetworkDataCache cache) {
		super(connection, cache);
	}

}
