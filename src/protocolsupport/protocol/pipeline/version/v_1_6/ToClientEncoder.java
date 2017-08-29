package protocolsupport.protocol.pipeline.version.v_1_6;

import net.md_5.bungee.protocol.Protocol;
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
import net.md_5.bungee.protocol.packet.StatusResponse;
import net.md_5.bungee.protocol.packet.TabCompleteResponse;
import net.md_5.bungee.protocol.packet.Team;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.packet.middleimpl.readable.login.v_6.EncryptionResponsePacket.FakeToClientEncrpytionResponse;
import protocolsupport.protocol.packet.middleimpl.writeable.NoopWriteablePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.login.v_6.EncryptionRequestPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.login.v_6.FakeToClientEncryptionReponsePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_6.KeepAlivePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_6.KickPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_6.LoginPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_6.PlayerListItemPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_6.PluginMessagePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_6.RespawnPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_6.ScoreboardDisplayPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_6.ScoreboardObjectivePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_6.ScoreboardTeamPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_6.TabCompleteResponsePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_6.ToClientChatPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.status.v_1_6.StatusResponsePacket;
import protocolsupport.protocol.pipeline.version.PacketEncoder;
import protocolsupport.protocol.storage.NetworkDataCache;

public class ToClientEncoder extends PacketEncoder {

	{
		registry.register(EncryptionRequest.class, EncryptionRequestPacket.class);
		registry.register(FakeToClientEncrpytionResponse.class, FakeToClientEncryptionReponsePacket.class);
		registry.register(LoginSuccess.class, NoopWriteablePacket.class);
		registry.register(Login.class, LoginPacket.class);
		registry.register(StatusResponse.class, StatusResponsePacket.class);
		registry.register(Kick.class, KickPacket.class);
		registry.register(KeepAlive.class, KeepAlivePacket.class);
		registry.register(PluginMessage.class, PluginMessagePacket.class);
		registry.register(Respawn.class, RespawnPacket.class);
		registry.register(Chat.class, ToClientChatPacket.class);
		registry.register(ScoreboardDisplay.class, ScoreboardDisplayPacket.class);
		registry.register(ScoreboardObjective.class, ScoreboardObjectivePacket.class);
		registry.register(Team.class, ScoreboardTeamPacket.class);
		registry.register(PlayerListItem.class, PlayerListItemPacket.class);
		registry.register(TabCompleteResponse.class, TabCompleteResponsePacket.class);
	}

	protected final Connection connection;
	protected final NetworkDataCache cache;

	public ToClientEncoder(Connection connection, NetworkDataCache cache) {
		super(Protocol.HANDSHAKE, true, ProtocolVersion.MINECRAFT_1_7_10.getId());
		this.connection = connection;
		this.cache = cache;
		registry.setCallBack(transformer -> {
			transformer.setConnection(this.connection);
			transformer.setSharedStorage(this.cache);
		});
	}

}
