package protocolsupport.protocol.packet.handler;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Level;

import javax.crypto.SecretKey;

import com.google.common.base.Preconditions;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.EncryptionUtil;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.connection.UpstreamBridge;
import net.md_5.bungee.http.HttpClient;
import net.md_5.bungee.jni.cipher.BungeeCipher;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.netty.cipher.CipherDecoder;
import net.md_5.bungee.netty.cipher.CipherEncoder;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.packet.EncryptionRequest;
import net.md_5.bungee.protocol.packet.EncryptionResponse;
import net.md_5.bungee.protocol.packet.LoginRequest;
import net.md_5.bungee.protocol.packet.LoginSuccess;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolType;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.api.events.PlayerLoginFinishEvent;
import protocolsupport.api.events.PlayerLoginStartEvent;
import protocolsupport.protocol.packet.middleimpl.readable.handshake.v_pe.LoginHandshakePacket;

public class PSInitialHandler extends InitialHandler {

	private final Connection connection;
	public PSInitialHandler(BungeeCord bungee, ListenerInfo listener, Connection connection) {
		super(bungee, listener);
		this.connection = connection;
	}

	protected ChannelWrapper channel;

	@Override
	public void connected(ChannelWrapper channel) throws Exception {
		super.connected(channel);
		this.channel = channel;
	}

	public ChannelWrapper getChannelWrapper() {
		return channel;
	}

	protected LoginState state = LoginState.HELLO;

	protected UUID uuid;

	@Override
	public void setUniqueId(final UUID uuid) {
		Preconditions.checkState((state == LoginState.HELLO) || (state == LoginState.ONLINEMODERESOLVE), "Can only set uuid while state is username");
		Preconditions.checkState(!isOnlineMode(), "Can only set uuid when online mode is false");
		this.uuid = uuid;
	}

	@Override
	public UUID getUniqueId() {
		return uuid;
	}

	@Override
	public String getUUID() {
		return this.uuid.toString().replaceAll("-", "");
	}

	protected String username;

	@Override
	public String getName() {
		return username;
	}

	protected EncryptionRequest request;

	@Override
	public void handle(LoginRequest loginRequest) throws Exception {
		Preconditions.checkState(state == LoginState.HELLO, "Not expecting USERNAME");
		state = LoginState.ONLINEMODERESOLVE;
		username = loginRequest.getData();
		if (this.getName().contains(".")) {
			this.disconnect(BungeeCord.getInstance().getTranslation("name_invalid"));
			return;
		}
		if (this.getName().length() > 16) {
			this.disconnect(BungeeCord.getInstance().getTranslation("name_too_long"));
			return;
		}
		final int limit = BungeeCord.getInstance().config.getPlayerLimit();
		if ((limit > 0) && (BungeeCord.getInstance().getOnlineCount() > limit)) {
			this.disconnect(BungeeCord.getInstance().getTranslation("proxy_full"));
			return;
		}
		if (!this.isOnlineMode() && (BungeeCord.getInstance().getPlayer(this.getUniqueId()) != null)) {
			this.disconnect(BungeeCord.getInstance().getTranslation("already_connected_proxy"));
			return;
		}
		BungeeCord.getInstance().getPluginManager().callEvent(new PreLoginEvent(this, new Callback<PreLoginEvent>() {
			@Override
			public void done(final PreLoginEvent result, final Throwable error) {
				if (result.isCancelled()) {
					disconnect(result.getCancelReasonComponents());
					return;
				}
				if (!isConnected()) {
					return;
				}
				processLoginStart();
			}
		}));
	}

	protected void processLoginStart() {
		PlayerLoginStartEvent event = new PlayerLoginStartEvent(connection, username, isOnlineMode(), getHandshake().getHost());
		ProxyServer.getInstance().getPluginManager().callEvent(event);
		if (event.isLoginDenied()) {
			disconnect(event.getDenyLoginMessage());
			return;
		}

		isOnlineMode = event.isOnlineMode();
		useOnlineModeUUID = event.useOnlineModeUUID();
		forcedUUID = event.getForcedUUID();

		switch (connection.getVersion().getProtocolType()) {
			case PC: {
				if (isOnlineMode()) {
					state = LoginState.KEY;
					unsafe().sendPacket((request = EncryptionUtil.encryptRequest()));
				} else {
					finishLogin();
				}
				break;
			}
			case PE: {
				if (isOnlineMode()) {
					String xuid = (String) connection.getMetadata(LoginHandshakePacket.XUID_METADATA_KEY);
					if (xuid == null) {
						disconnect("This server is in online mode, but no valid XUID was found (XBOX live auth required)");
						return;
					} else {
						uuid = new UUID(0, Long.parseLong(xuid));
					}
				}
				finishLogin();
				break;
			}
			default: {
				throw new IllegalArgumentException(MessageFormat.format("Unknown protocol type {0}", connection.getVersion().getProtocolType()));
			}
		}
	}

	protected LoginResult loginProfile;

	@Override
	public LoginResult getLoginProfile() {
		return loginProfile;
	}

	@Override
	public void handle(final EncryptionResponse encryptResponse) throws Exception {
		Preconditions.checkState(state == LoginState.KEY, "Not expecting ENCRYPT");
		state = LoginState.AUTHENTICATING;
		SecretKey sharedKey = EncryptionUtil.getSecret(encryptResponse, request);
		BungeeCipher decrypt = EncryptionUtil.getCipher(false, sharedKey);
		channel.addBefore(PipelineUtils.FRAME_DECODER, PipelineUtils.DECRYPT_HANDLER, new CipherDecoder(decrypt));
		if (isFullEncryption(connection.getVersion())) {
			BungeeCipher encrypt = EncryptionUtil.getCipher(true, sharedKey);
			channel.addBefore(PipelineUtils.FRAME_PREPENDER, PipelineUtils.ENCRYPT_HANDLER, new CipherEncoder(encrypt));
		}
		String encName = URLEncoder.encode(this.getName(), "UTF-8");
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		for (final byte[] bit : new byte[][] { this.request.getServerId().getBytes("ISO_8859_1"), sharedKey.getEncoded(), EncryptionUtil.keys.getPublic().getEncoded() }) {
			sha.update(bit);
		}
		String encodedHash = URLEncoder.encode(new BigInteger(sha.digest()).toString(16), "UTF-8");
		String preventProxy = BungeeCord.getInstance().config.isPreventProxyConnections() ? ("&ip=" + URLEncoder.encode(this.getAddress().getAddress().getHostAddress(), "UTF-8")) : "";
		String authURL = "https://sessionserver.mojang.com/session/minecraft/hasJoined?username=" + encName + "&serverId=" + encodedHash + preventProxy;
		Callback<String> handler = new Callback<String>() {
			@Override
			public void done(final String result, final Throwable error) {
				if (error == null) {
					final LoginResult obj = BungeeCord.getInstance().gson.fromJson(result, LoginResult.class);
					if ((obj != null) && (obj.getId() != null)) {
						loginProfile = obj;
						username = obj.getName();
						uuid = Util.getUUID(obj.getId());
						finishLogin();
						return;
					}
					disconnect(BungeeCord.getInstance().getTranslation("offline_mode_player"));
				} else {
					disconnect(BungeeCord.getInstance().getTranslation("mojang_fail"));
					BungeeCord.getInstance().getLogger().log(Level.SEVERE, "Error authenticating " + getName() + " with minecraft.net", error);
				}
			}
		};
		HttpClient.get(authURL, channel.getHandle().eventLoop(), handler);
	}

	protected boolean isOnlineMode = BungeeCord.getInstance().config.isOnlineMode();

	@Override
	public void setOnlineMode(boolean onlineMode) {
		Preconditions.checkState((state == LoginState.HELLO) || (state == LoginState.ONLINEMODERESOLVE), "Can only set uuid while state is username");
		this.isOnlineMode = onlineMode;
	}

	@Override
	public boolean isOnlineMode() {
		return isOnlineMode;
	}

	protected boolean useOnlineModeUUID = isOnlineMode;
	protected UUID forcedUUID;

	protected UUID offlineuuid;

	@Override
	public UUID getOfflineId() {
		return offlineuuid;
	}

	@SuppressWarnings("deprecation")
	protected void finishLogin() {
		offlineuuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + this.getName()).getBytes(StandardCharsets.UTF_8));
		if ((isOnlineMode() && !useOnlineModeUUID) || (uuid == null)) {
			uuid = offlineuuid;
		}
		if (forcedUUID != null) {
			uuid = forcedUUID;
		}
		if (isOnlineMode()) {
			final ProxiedPlayer oldName = BungeeCord.getInstance().getPlayer(this.getName());
			if (oldName != null) {
				oldName.disconnect(BungeeCord.getInstance().getTranslation("already_connected_proxy"));
			}
			final ProxiedPlayer oldID = BungeeCord.getInstance().getPlayer(this.getUniqueId());
			if (oldID != null) {
				oldID.disconnect(BungeeCord.getInstance().getTranslation("already_connected_proxy"));
			}
		} else {
			final ProxiedPlayer oldName = BungeeCord.getInstance().getPlayer(this.getName());
			if (oldName != null) {
				this.disconnect(BungeeCord.getInstance().getTranslation("already_connected_proxy"));
				return;
			}
		}
		final Callback<LoginEvent> complete = new Callback<LoginEvent>() {
			@Override
			public void done(final LoginEvent result, final Throwable error) {
				if (result.isCancelled()) {
					disconnect(result.getCancelReasonComponents());
					return;
				}
				if (!isConnected()) {
					return;
				}
				channel.getHandle().eventLoop().execute(() -> processLoginFinish());
			}
		};
		BungeeCord.getInstance().getPluginManager().callEvent(new LoginEvent(this, complete));
	}

	@SuppressWarnings("deprecation")
	protected void processLoginFinish() {
		if (!channel.isClosing()) {
			UserConnection userCon = new UserConnection(BungeeCord.getInstance(), channel, getName(), PSInitialHandler.this);
			userCon.setCompressionThreshold(BungeeCord.getInstance().config.getCompressionThreshold());
			userCon.init();
			unsafe().sendPacket(new LoginSuccess(getUniqueId().toString(), getName()));
			channel.setProtocol(Protocol.GAME);

			PlayerLoginFinishEvent event = new PlayerLoginFinishEvent(connection, getName(), getUniqueId(), isOnlineMode());
			BungeeCord.getInstance().getPluginManager().callEvent(event);
			if (event.isLoginDenied()) {
				disconnect(event.getDenyLoginMessage());
				return;
			}

			channel.getHandle().pipeline().get(HandlerBoss.class).setHandler(new UpstreamBridge(BungeeCord.getInstance(), userCon));
			BungeeCord.getInstance().getPluginManager().callEvent(new PostLoginEvent(userCon));

			ServerInfo server;
			if (BungeeCord.getInstance().getReconnectHandler() != null) {
				server = BungeeCord.getInstance().getReconnectHandler().getServer(userCon);
			} else {
				server = AbstractReconnectHandler.getForcedHost(PSInitialHandler.this);
			}
			if (server == null) {
				server = BungeeCord.getInstance().getServerInfo(getListener().getDefaultServer());
			}
			userCon.connect(server, null, true);
		}
	}

	public enum LoginState {
		HELLO, ONLINEMODERESOLVE, KEY, AUTHENTICATING;
	}

	protected static boolean isFullEncryption(ProtocolVersion version) {
		return (version.getProtocolType() == ProtocolType.PC) && version.isAfterOrEq(ProtocolVersion.MINECRAFT_1_7_5);
	}

}
