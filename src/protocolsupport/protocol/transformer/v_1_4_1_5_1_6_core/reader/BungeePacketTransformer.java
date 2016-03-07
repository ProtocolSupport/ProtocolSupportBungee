package protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.reader;

import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.protocol.transformer.TransformedPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.ChatPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.EncryptionRequestPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.KeepAlivePacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.KickPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.LoginPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.PlayerListItemPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.PluginMessagePacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.RespawnPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.ScoreboardDispayPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.ScoreboardObjectivePacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.ScoreboardScorePacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.TeamPacket;
import protocolsupport.utils.PingSerializer;
import protocolsupport.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.packet.Chat;
import net.md_5.bungee.protocol.packet.EncryptionRequest;
import net.md_5.bungee.protocol.packet.KeepAlive;
import net.md_5.bungee.protocol.packet.Kick;
import net.md_5.bungee.protocol.packet.Login;
import net.md_5.bungee.protocol.packet.LoginSuccess;
import net.md_5.bungee.protocol.packet.PlayerListHeaderFooter;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import net.md_5.bungee.protocol.packet.PluginMessage;
import net.md_5.bungee.protocol.packet.Respawn;
import net.md_5.bungee.protocol.packet.ScoreboardDisplay;
import net.md_5.bungee.protocol.packet.ScoreboardObjective;
import net.md_5.bungee.protocol.packet.ScoreboardScore;
import net.md_5.bungee.protocol.packet.SetCompression;
import net.md_5.bungee.protocol.packet.StatusResponse;
import net.md_5.bungee.protocol.packet.Team;
import net.md_5.bungee.protocol.packet.Title;

public class BungeePacketTransformer {

	private static final TransformedPacket[] EMPTY = new TransformedPacket[0];

	public static TransformedPacket[] transformBungeePacket(Channel channel, DefinedPacket packet, ByteBuf buf) {
		if (packet instanceof KeepAlive) {
			return new TransformedPacket[] { new KeepAlivePacket(((KeepAlive) packet).getRandomId()) };
		} else 
		if (packet instanceof PluginMessage) {
			PluginMessage pmessage = (PluginMessage) packet;
			return new TransformedPacket[] { new PluginMessagePacket(pmessage.getTag(), pmessage.getData().clone(), pmessage.isAllowExtendedPacket()) };
		} else
		if (packet instanceof Chat) {
			Chat chat = (Chat) packet;
			return new TransformedPacket[] { new ChatPacket(Utils.toLegacyText(chat.getMessage())) };
		} else
		if (packet instanceof Respawn) {
			Respawn respawn = (Respawn) packet;
			return new TransformedPacket[] { new RespawnPacket(respawn.getDimension(), respawn.getDifficulty(), respawn.getGameMode(), respawn.getLevelType()) };
		} else
		if (packet instanceof PlayerListItem) {
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
					return EMPTY;
				}
			}
		} else
		if (packet instanceof Kick) {
			Kick kick = (Kick) packet;
			return new TransformedPacket[] { new KickPacket(Utils.toLegacyText(kick.getMessage())) };
		} else
		if (packet instanceof LoginSuccess || packet instanceof Title || packet instanceof PlayerListHeaderFooter || packet instanceof SetCompression) {
			return EMPTY;
		} else
		if (packet instanceof StatusResponse) {
			StatusResponse status = (StatusResponse) packet;
			return new TransformedPacket[] { new KickPacket(PingSerializer.fromJSON(ProtocolSupportAPI.getProtocolVersion(channel.remoteAddress()).getId(), status.getResponse())) };
		} else
		if (packet instanceof EncryptionRequest) {
			EncryptionRequest erequest = (EncryptionRequest) packet;
			return new TransformedPacket[] { new EncryptionRequestPacket(erequest.getServerId(), erequest.getVerifyToken()) };
		} else
		if (packet instanceof Login) {
			Login login = (Login) packet;
			return new TransformedPacket[] { new LoginPacket(login.getEntityId(), login.getGameMode(), (byte) login.getDimension(), login.getDifficulty(), login.getMaxPlayers(), login.getLevelType()) };
		} else
		if (packet instanceof Team) {
			Team team = (Team) packet;
			return new TransformedPacket[] { new TeamPacket(team.getName(), team.getMode(), team.getDisplayName(), team.getPrefix(), team.getSuffix(), team.getFriendlyFire(), team.getPlayers()) };
		} else
		if (packet instanceof ScoreboardDisplay) {
			ScoreboardDisplay sdisplay = (ScoreboardDisplay) packet;
			return new TransformedPacket[] { new ScoreboardDispayPacket(sdisplay.getPosition(), sdisplay.getName()) };
		} else
		if (packet instanceof ScoreboardObjective) {
			ScoreboardObjective sobjective = (ScoreboardObjective) packet;
			return new TransformedPacket[] { new ScoreboardObjectivePacket(sobjective.getName(), sobjective.getValue(), sobjective.getAction()) };
		} else
		if (packet instanceof ScoreboardScore) {
			ScoreboardScore sscore = (ScoreboardScore) packet;
			return new TransformedPacket[] { new ScoreboardScorePacket(sscore.getItemName(), sscore.getAction(), sscore.getScoreName(), sscore.getValue()) };
		}
		return null;
	}

}
