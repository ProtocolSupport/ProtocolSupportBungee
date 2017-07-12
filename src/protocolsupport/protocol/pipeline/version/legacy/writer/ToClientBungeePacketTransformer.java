package protocolsupport.protocol.pipeline.version.legacy.writer;

import net.md_5.bungee.protocol.DefinedPacket;
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
import net.md_5.bungee.protocol.packet.Team;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.pipeline.version.legacy.packets.ChatPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.EncryptionRequestPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.KeepAlivePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.KickPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.LoginPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.PlayerListItemPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.PluginMessagePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.RespawnPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.ScoreboardDispayPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.ScoreboardObjectivePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.ScoreboardScorePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.TeamPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.TransformedPacket;
import protocolsupport.utils.PingSerializer;
import protocolsupport.utils.Utils;

public class ToClientBungeePacketTransformer implements BungeePacketTransformer {

	@Override
	public TransformedPacket[] transformBungeePacket(ProtocolVersion version, DefinedPacket packet) {
		if (packet instanceof KeepAlive) {
			return new TransformedPacket[] { new KeepAlivePacket(((KeepAlive) packet).getRandomId()) };
		} else if (packet instanceof PluginMessage) {
			PluginMessage pmessage = (PluginMessage) packet;
			return new TransformedPacket[] { new PluginMessagePacket(pmessage.getTag(), pmessage.getData().clone(), pmessage.isAllowExtendedPacket()) };
		} else if (packet instanceof Chat) {
			Chat chat = (Chat) packet;
			String message = Utils.toLegacyText(chat.getMessage());
			switch (version) {
				case MINECRAFT_1_6_4:
				case MINECRAFT_1_6_2:
				case MINECRAFT_1_6_1: {
					message = "{\"text\":\"" + message + "\"}";
					break;
				}
				default: {
					break;
				}
			}
			return new TransformedPacket[] { new ChatPacket(message) };
		} else if (packet instanceof Respawn) {
			Respawn respawn = (Respawn) packet;
			return new TransformedPacket[] { new RespawnPacket(respawn.getDimension(), respawn.getDifficulty(), respawn.getGameMode(), respawn.getLevelType()) };
		} else if (packet instanceof PlayerListItem) {
			PlayerListItem listitem = (PlayerListItem) packet;
			switch (listitem.getAction()) {
				case ADD_PLAYER:
				case REMOVE_PLAYER:
				case UPDATE_DISPLAY_NAME: {
					TransformedPacket[] packets = new TransformedPacket[listitem.getItems().length];
					for (int i = 0; i < packets.length; i++) {
						packets[i] = new PlayerListItemPacket(listitem.getAction(), listitem.getItems()[i]);
					}
					return packets;
				}
				default: {
					return new TransformedPacket[0];
				}
			}
		} else if (packet instanceof Kick) {
			Kick kick = (Kick) packet;
			return new TransformedPacket[] { new KickPacket(Utils.toLegacyText(kick.getMessage())) };
		} else if (packet instanceof StatusResponse) {
			StatusResponse status = (StatusResponse) packet;
			return new TransformedPacket[] { new KickPacket(PingSerializer.fromJSON(version.getId(), status.getResponse())) };
		} else if (packet instanceof EncryptionRequest) {
			EncryptionRequest erequest = (EncryptionRequest) packet;
			return new TransformedPacket[] { new EncryptionRequestPacket(erequest.getServerId(), erequest.getPublicKey(), erequest.getVerifyToken()) };
		} else if (packet instanceof LoginSuccess) {
			return new TransformedPacket[0];
		} if (packet instanceof Login) {
			Login login = (Login) packet;
			return new TransformedPacket[] { new LoginPacket(login.getEntityId(), login.getGameMode(), (byte) login.getDimension(), login.getDifficulty(), login.getMaxPlayers(), login.getLevelType()) };
		} else if (packet instanceof Team) {
			Team team = (Team) packet;
			return new TransformedPacket[] { new TeamPacket(team.getName(), team.getMode(), team.getDisplayName(), team.getPrefix(), team.getSuffix(), team.getFriendlyFire(), team.getPlayers()) };
		} else if (packet instanceof ScoreboardDisplay) {
			ScoreboardDisplay sdisplay = (ScoreboardDisplay) packet;
			return new TransformedPacket[] { new ScoreboardDispayPacket(sdisplay.getPosition(), sdisplay.getName()) };
		} else if (packet instanceof ScoreboardObjective) {
			ScoreboardObjective sobjective = (ScoreboardObjective) packet;
			return new TransformedPacket[] { new ScoreboardObjectivePacket(sobjective.getName(), sobjective.getValue(), sobjective.getAction()) };
		} else if (packet instanceof ScoreboardScore) {
			ScoreboardScore sscore = (ScoreboardScore) packet;
			return new TransformedPacket[] { new ScoreboardScorePacket(sscore.getItemName(), sscore.getAction(), sscore.getScoreName(), sscore.getValue()) };
		} else {
			throw new IllegalArgumentException("Unable to transform bungee packet " + packet.getClass().getName());
		}
	}

	@Override
	public TransformedPacket[] transformTransformedPacket(ProtocolVersion version, TransformedPacket packet) {
		return new TransformedPacket[] { packet };
	}

}
