package protocolsupport.protocol.pipeline.version.v_1_6;

import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.packet.Chat;
import net.md_5.bungee.protocol.packet.ClientStatus;
import net.md_5.bungee.protocol.packet.Handshake;
import net.md_5.bungee.protocol.packet.KeepAlive;
import net.md_5.bungee.protocol.packet.LoginRequest;
import net.md_5.bungee.protocol.packet.PluginMessage;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_6.ClientCommandPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_6.HandshakeCachePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_6.KeepAlivePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_6.LoginRequestServerHandshakePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_6.PluginMessagePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_6.ToServerChatPacket;
import protocolsupport.protocol.pipeline.version.PacketEncoder;
import protocolsupport.protocol.storage.NetworkDataCache;

public class ToServerEncoder extends PacketEncoder {

	{
		registry.register(Handshake.class, HandshakeCachePacket.class);
		registry.register(LoginRequest.class, LoginRequestServerHandshakePacket.class);
		registry.register(KeepAlive.class, KeepAlivePacket.class);
		registry.register(Chat.class, ToServerChatPacket.class);
		registry.register(PluginMessage.class, PluginMessagePacket.class);
		registry.register(ClientStatus.class, ClientCommandPacket.class);
	}

	protected final Connection connection;
	protected final NetworkDataCache cache;

	public ToServerEncoder(Connection connection, NetworkDataCache cache) {
		super(Protocol.HANDSHAKE, true, ProtocolVersion.MINECRAFT_1_7_10.getId());
		this.connection = connection;
		this.cache = cache;
		registry.setCallBack(transformer -> {
			transformer.setConnection(this.connection);
			transformer.setSharedStorage(this.cache);
		});
	}

}
