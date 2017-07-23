package protocolsupport.protocol.pipeline.version.legacy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.MinecraftEncoder;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.packet.Chat;
import net.md_5.bungee.protocol.packet.EncryptionRequest;
import net.md_5.bungee.protocol.packet.Handshake;
import net.md_5.bungee.protocol.packet.KeepAlive;
import net.md_5.bungee.protocol.packet.Kick;
import net.md_5.bungee.protocol.packet.Login;
import net.md_5.bungee.protocol.packet.LoginSuccess;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import net.md_5.bungee.protocol.packet.PlayerListItem.Item;
import net.md_5.bungee.protocol.packet.PluginMessage;
import net.md_5.bungee.protocol.packet.Respawn;
import net.md_5.bungee.protocol.packet.ScoreboardDisplay;
import net.md_5.bungee.protocol.packet.ScoreboardObjective;
import net.md_5.bungee.protocol.packet.ScoreboardScore;
import net.md_5.bungee.protocol.packet.StatusResponse;
import net.md_5.bungee.protocol.packet.Team;
import protocolsupport.LoggerUtil;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.pipeline.version.legacy.packets.ChatPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.ClientStatusPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.EncryptionRequestPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.HandshakePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.KeepAlivePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.KickPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.LoginPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.LoginRequestPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.PlayerListItemPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.PluginMessagePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.RespawnPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.ScoreboardDispayPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.ScoreboardObjectivePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.ScoreboardScorePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.TeamPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.TransformedPacket;
import protocolsupport.protocol.storage.SharedStorage;
import protocolsupport.utils.ClassMap;
import protocolsupport.utils.PingSerializer;
import protocolsupport.utils.Utils;

public class PacketEncoder extends MinecraftEncoder {

	@FunctionalInterface
	private static interface PacketTransformer {
		public Collection<TransformedPacket> transform(Connection connection, SharedStorage storage, DefinedPacket packet);
	}

	private static final ClassMap<PacketTransformer> toClientTransformers = new ClassMap<>();
	static {
		toClientTransformers.register(TransformedPacket.class, (connection, storage, packet) -> Collections.singletonList((TransformedPacket) packet));
		toClientTransformers.register(LoginSuccess.class, (connection, storage, packet) -> Collections.emptyList());
		toClientTransformers.register(KeepAlive.class, (connection, storage, packet) -> Collections.singletonList(new KeepAlivePacket(((KeepAlive) packet).getRandomId())));
		toClientTransformers.register(Kick.class, (connection, storage, packet) -> Collections.singletonList(new KickPacket(Utils.toLegacyText(((Kick) packet).getMessage()))));
		toClientTransformers.register(PluginMessage.class, (connection, storage, packet) -> {
			PluginMessage pmessage = (PluginMessage) packet;
			return Collections.singletonList(new PluginMessagePacket(pmessage.getTag(), pmessage.getData().clone(), pmessage.isAllowExtendedPacket()));
		});
		toClientTransformers.register(Respawn.class, (connection, storage, packet) -> {
			Respawn respawn = (Respawn) packet;
			return Collections.singletonList(new RespawnPacket(respawn.getDimension(), respawn.getDifficulty(), respawn.getGameMode(), respawn.getLevelType()));
		});
		toClientTransformers.register(StatusResponse.class, (connection, storage, packet) -> {
			StatusResponse status = (StatusResponse) packet;
			return Collections.singletonList(new KickPacket(PingSerializer.fromJSON(connection.getVersion().getId(), status.getResponse())));
		});
		toClientTransformers.register(EncryptionRequest.class, (connection, storage, packet) -> {
			EncryptionRequest erequest = (EncryptionRequest) packet;
			return Collections.singletonList(new EncryptionRequestPacket(erequest.getServerId(), erequest.getPublicKey(), erequest.getVerifyToken()));
		});
		toClientTransformers.register(Login.class, (connection, storage, packet) -> {
			Login login = (Login) packet;
			return Collections.singletonList(new LoginPacket(login.getEntityId(), login.getGameMode(), (byte) login.getDimension(), login.getDifficulty(), login.getMaxPlayers(), login.getLevelType()));
		});
		toClientTransformers.register(Team.class, (connection, storage, packet) -> {
			Team team = (Team) packet;
			return Collections.singletonList(new TeamPacket(team.getName(), team.getMode(), team.getDisplayName(), team.getPrefix(), team.getSuffix(), team.getFriendlyFire(), team.getPlayers()));
		});
		toClientTransformers.register(ScoreboardDisplay.class, (connection, storage, packet) -> {
			ScoreboardDisplay sdisplay = (ScoreboardDisplay) packet;
			return Collections.singletonList(new ScoreboardDispayPacket(sdisplay.getPosition(), sdisplay.getName()));
		});
		toClientTransformers.register(ScoreboardObjective.class, (connection, storage, packet) -> {
			ScoreboardObjective sobjective = (ScoreboardObjective) packet;
			return Collections.singletonList(new ScoreboardObjectivePacket(sobjective.getName(), sobjective.getValue(), sobjective.getAction()));
		});
		toClientTransformers.register(ScoreboardScore.class, (connection, storage, packet) -> {
			ScoreboardScore sscore = (ScoreboardScore) packet;
			return Collections.singletonList(new ScoreboardScorePacket(sscore.getItemName(), sscore.getAction(), sscore.getScoreName(), sscore.getValue()));
		});
		toClientTransformers.register(Chat.class, (connection, storage, packet) -> {
			Chat chat = (Chat) packet;
			String message = Utils.toLegacyText(chat.getMessage());
			return Collections.singletonList(
				connection.getVersion().isBetween(ProtocolVersion.MINECRAFT_1_6_1, ProtocolVersion.MINECRAFT_1_6_4) ?
				new ChatPacket("{\"text\":\"" + message + "\"}") : new ChatPacket(message)
			);
		});
		toClientTransformers.register(PlayerListItem.class, (connection, storage, packet) -> {
			PlayerListItem listitem = (PlayerListItem) packet;
			switch (listitem.getAction()) {
				case ADD_PLAYER:
				case REMOVE_PLAYER:
				case UPDATE_DISPLAY_NAME: {
					ArrayList<TransformedPacket> packets = new ArrayList<>();
					for (Item item : listitem.getItems()) {
						packets.add(new PlayerListItemPacket(listitem.getAction(), item));
					}
					return packets;
				}
				default: {
					return Collections.emptyList();
				}
			}
		});
	}

	private static final ClassMap<PacketTransformer> toServerTransformers = new ClassMap<>();
	static {
		toServerTransformers.register(LoginRequestPacket.class, (connection, storage, packet) -> Collections.singletonList(
			new HandshakePacket(connection.getVersion().getId(), ((LoginRequestPacket) packet).getData(), storage.cachedHandshake.getHost(), storage.cachedHandshake.getPort())
		));
		toServerTransformers.register(ClientStatusPacket.class, (connection, storage, packet) -> {
			ClientStatusPacket status = ((ClientStatusPacket) packet);
			if (status.getPayload() == 1) {
				return Collections.singletonList(status);
			} else {
				return Collections.emptyList();
			}
		});
		toServerTransformers.register(TransformedPacket.class, (connection, storage, packet) -> Collections.singletonList((TransformedPacket) packet));
		toServerTransformers.register(KeepAlive.class, (connection, storage, packet) -> Collections.singletonList(new KeepAlivePacket(((KeepAlive) packet).getRandomId())));
		toServerTransformers.register(PluginMessage.class, (connection, storage, packet) -> {
			PluginMessage pmessage = (PluginMessage) packet;
			return Collections.singletonList(new PluginMessagePacket(pmessage.getTag(), pmessage.getData().clone(), pmessage.isAllowExtendedPacket()));
		});
		toServerTransformers.register(Chat.class, (connection, storage, packet) -> Collections.singletonList(new ChatPacket(((Chat) packet).getMessage())));
		toServerTransformers.register(Handshake.class, (connection, storage, packet) -> {
			storage.cachedHandshake = (Handshake) packet;
			return Collections.emptyList();
		});
	}

	private final Connection connection;
	private final SharedStorage storage;
	private final boolean toclient;
	private final ClassMap<PacketTransformer> transformers;

	public PacketEncoder(Protocol protocol, boolean toclient, Connection connection, SharedStorage storage) {
		super(protocol, toclient, ProtocolVersion.MINECRAFT_1_7_10.getId());
		this.connection = connection;
		this.storage = storage;
		this.toclient = toclient;
		this.transformers = toclient ? toClientTransformers : toServerTransformers;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, DefinedPacket packet, ByteBuf buf) throws Exception {
		for (TransformedPacket tpacket : transformers.get(packet.getClass()).transform(connection, storage, packet)) {
			if (LoggerUtil.isEnabled()) {
				LoggerUtil.debug((toclient ? "[To Client] " : "[To Server] ") + "Sent packet(id: " + tpacket.getId() + ", defined data: " + Utils.toStringAllFields(tpacket) + ")");
			}
			buf.writeByte(tpacket.getId());
			tpacket.write(buf);
		}
	}

}
