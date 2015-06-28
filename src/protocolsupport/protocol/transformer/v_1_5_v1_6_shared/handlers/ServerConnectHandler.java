package protocolsupport.protocol.transformer.v_1_5_v1_6_shared.handlers;

import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import com.google.common.base.Preconditions;

import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.protocol.CheckedChannelWrapper;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets.HandshakePacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets.LoginPacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets.PluginMessagePacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets.RespawnPacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets.ScoreboardObjectivePacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets.TeamPacket;
import protocolsupport.utils.FakeChannelContext;
import protocolsupport.utils.ReflectionUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.score.Objective;
import net.md_5.bungee.api.score.Scoreboard;
import net.md_5.bungee.api.score.Team;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.connection.CancelSendSignal;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.forge.ForgeServerHandler;
import net.md_5.bungee.forge.ForgeUtils;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.PacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.MinecraftOutput;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.packet.EncryptionRequest;
import net.md_5.bungee.protocol.packet.Handshake;
import net.md_5.bungee.protocol.packet.Kick;
import net.md_5.bungee.protocol.packet.Login;
import net.md_5.bungee.protocol.packet.LoginSuccess;
import net.md_5.bungee.protocol.packet.PluginMessage;
import net.md_5.bungee.protocol.packet.SetCompression;

public class ServerConnectHandler extends PacketHandler {

	private final ProxyServer bungee;
	private ChannelWrapper ch;
	private final UserConnection user;
	private final BungeeServerInfo target;

	public ServerConnectHandler(ProxyServer bungee, UserConnection user, BungeeServerInfo target) {
		this.bungee = bungee;
		this.user = user;
		this.target = target;
	}

	private State thisState = State.LOGIN;
	private ForgeServerHandler handshakeHandler;

	public ForgeServerHandler getHandshakeHandler() {
		return this.handshakeHandler;
	}

	private static enum State {
		LOGIN, FINISHED;

		private State() {
		}
	}

	@Override
	public void exception(Throwable t) throws Exception {
		String message = "Exception Connecting:" + Util.exception(t);
		if (this.user.getServer() == null) {
			this.user.disconnect(message);
		} else {
			this.user.sendMessage(ChatColor.RED + message);
		}
	}

	@Override
	public void connected(ChannelWrapper channel) throws Exception {
		this.ch = new CheckedChannelWrapper(new FakeChannelContext(channel.getHandle()));

		this.handshakeHandler = new ForgeServerHandler(this.user, this.ch, this.target);
		Handshake originalHandshake = this.user.getPendingConnection().getHandshake();
		HandshakePacket copiedHandshake = new HandshakePacket(ProtocolSupportAPI.getProtocolVersion(this.user.getAddress()).getId(), this.user.getPendingConnection().getLoginRequest().getData(), originalHandshake.getHost(), originalHandshake.getPort());
		if (BungeeCord.getInstance().config.isIpForward()) {
			String newHost = copiedHandshake.getHost() + "\000" + this.user.getAddress().getHostString() + "\000" + this.user.getUUID();
			LoginResult profile = this.user.getPendingConnection().getLoginProfile();
			if ((profile != null) && (profile.getProperties() != null) && (profile.getProperties().length > 0)) {
				newHost = newHost + "\000" + BungeeCord.getInstance().gson.toJson(profile.getProperties());
			}
			copiedHandshake.setHost(newHost);
		} else if (!this.user.getExtraDataInHandshake().isEmpty()) {
			copiedHandshake.setHost(copiedHandshake.getHost() + this.user.getExtraDataInHandshake());
		}
		ch.write(copiedHandshake);

		ch.setProtocol(Protocol.LOGIN);
	}

	@Override
	public void disconnected(ChannelWrapper channel) throws Exception {
		this.user.getPendingConnects().remove(this.target);
	}

	@Override
	public void handle(LoginSuccess loginSuccess) throws Exception {
		throw new RuntimeException("Server sent LoginSucess, that shouldn't have happened");
	}

	@Override
	public void handle(SetCompression setCompression) throws Exception {
		throw new RuntimeException("Server sent SetCompression, that shouldn't have happened");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void handle(Login login) throws Exception {
		Preconditions.checkState(this.thisState == State.LOGIN, "Not expecting LOGIN");

		ServerConnection server = new ServerConnection(this.ch, this.target);
		ServerConnectedEvent event = new ServerConnectedEvent(this.user, server);
		this.bungee.getPluginManager().callEvent(event);

		this.ch.write(BungeeCord.getInstance().registerChannels());
		Queue<DefinedPacket> packetQueue = this.target.getPacketQueue();
		synchronized (packetQueue) {
			while (!packetQueue.isEmpty()) {
				this.ch.write(packetQueue.poll());
			}
		}
		for (PluginMessage message : this.user.getPendingConnection().getRegisterMessages()) {
			this.ch.write(message);
		}
		if (this.user.getSettings() != null) {
			this.ch.write(this.user.getSettings());
		}
		if ((this.user.getForgeClientHandler().getClientModList() == null) && (!this.user.getForgeClientHandler().isHandshakeComplete())) {
			this.user.getForgeClientHandler().setHandshakeComplete();
		}
		if (this.user.getServer() == null) {
			this.user.setClientEntityId(login.getEntityId());
			this.user.setServerEntityId(login.getEntityId());

			LoginPacket modLogin = new LoginPacket(login.getEntityId(), login.getGameMode(), (byte) login.getDimension(), login.getDifficulty(), (short) (byte) this.user.getPendingConnection().getListener().getTabListSize(), login.getLevelType());

			this.user.unsafe().sendPacket(modLogin);
			MinecraftOutput out = new MinecraftOutput();
			out.writeStringUTF8WithoutLengthHeaderBecauseDinnerboneStuffedUpTheMCBrandPacket(ProxyServer.getInstance().getName() + " (" + ProxyServer.getInstance().getVersion() + ")");
			this.user.unsafe().sendPacket(new PluginMessagePacket("MC|Brand", out.toArray(), this.handshakeHandler.isServerForge()));
		} else {
			this.user.getTabListHandler().onServerChange();

			Scoreboard serverScoreboard = this.user.getServerSentScoreboard();
			for (Objective objective : serverScoreboard.getObjectives()) {
				this.user.unsafe().sendPacket(new ScoreboardObjectivePacket(objective.getName(), objective.getValue(), (byte) 1));
			}
			for (Team team : serverScoreboard.getTeams()) {
				this.user.unsafe().sendPacket(new TeamPacket(team.getName()));
			}
			serverScoreboard.clear();

			try {
				ReflectionUtils.invokeMethod(this.user, "sendDimensionSwitch");
			} catch (Throwable t) {
				t.printStackTrace();
				user.disconnect("Failed to send dimension switch");
				return;
			}

			this.user.setServerEntityId(login.getEntityId());
			this.user.unsafe().sendPacket(new RespawnPacket(login.getDimension(), login.getDifficulty(), login.getGameMode(), login.getLevelType()));

			this.user.getServer().setObsolete(true);
			this.user.getServer().disconnect("Quitting");
		}
		if (!this.user.isActive()) {
			server.disconnect("Quitting");

			this.bungee.getLogger().warning("No client connected for pending server!");
			return;
		}
		this.target.addPlayer(this.user);
		this.user.getPendingConnects().remove(this.target);
		this.user.setDimensionChange(false);

		this.user.setServer(server);
		(this.ch.getHandle().pipeline().get(HandlerBoss.class)).setHandler(new EntityRewriteDownstreamBridge(this.bungee, this.user, server));

		this.bungee.getPluginManager().callEvent(new ServerSwitchEvent(this.user));

		this.thisState = State.FINISHED;

		throw CancelSendSignal.INSTANCE;
	}

	@Override
	public void handle(EncryptionRequest encryptionRequest) throws Exception {
		throw new RuntimeException("Server is online mode!");
	}

	@Override
	public void handle(Kick kick) throws Exception {
		ServerInfo def = this.bungee.getServerInfo(this.user.getPendingConnection().getListener().getFallbackServer());
		if (Objects.equals(this.target, def)) {
			def = null;
		}
		ServerKickEvent event = this.bungee.getPluginManager().callEvent(new ServerKickEvent(this.user, this.target, ComponentSerializer.parse(kick.getMessage()), def, ServerKickEvent.State.CONNECTING));
		if ((event.isCancelled()) && (event.getCancelServer() != null)) {
			this.user.connect(event.getCancelServer());
			throw CancelSendSignal.INSTANCE;
		}
		@SuppressWarnings("deprecation")
		String message = this.bungee.getTranslation("connect_kick", new Object[] { this.target.getName(), event.getKickReason() });
		if (this.user.isDimensionChange()) {
			this.user.disconnect(message);
		} else {
			this.user.sendMessage(message);
		}
		throw CancelSendSignal.INSTANCE;
	}

	@Override
	public void handle(PluginMessage pluginMessage) throws Exception {
		if (pluginMessage.getTag().equals("REGISTER")) {
			Set<String> channels = ForgeUtils.readRegisteredChannels(pluginMessage);
			boolean isForgeServer = false;
			for (String channel : channels) {
				if (channel.equals("FML|HS")) {
					if ((this.user.getServer() != null) && (this.user.getForgeClientHandler().isHandshakeComplete())) {
						this.user.getForgeClientHandler().resetHandshake();
					}
					isForgeServer = true;
					break;
				}
			}
			if ((isForgeServer) && (!this.handshakeHandler.isServerForge())) {
				this.handshakeHandler.setServerAsForgeServer();
				this.user.setForgeServerHandler(this.handshakeHandler);
			}
		}
		if ((pluginMessage.getTag().equals("FML|HS")) || (pluginMessage.getTag().equals("FORGE"))) {
			this.handshakeHandler.handle(pluginMessage);
			if (this.user.getForgeClientHandler().checkUserOutdated()) {
				this.ch.close();
				this.user.getPendingConnects().remove(this.target);
			}
			throw CancelSendSignal.INSTANCE;
		}
		this.user.unsafe().sendPacket(pluginMessage);
	}

	@Override
	public String toString() {
		return "[" + this.user.getName() + "] <-> ServerConnector [" + this.target.getName() + "]";
	}

}
