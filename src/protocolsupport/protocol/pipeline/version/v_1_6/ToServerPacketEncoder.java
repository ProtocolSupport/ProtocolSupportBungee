package protocolsupport.protocol.pipeline.version.v_1_6;

import net.md_5.bungee.protocol.packet.Chat;
import net.md_5.bungee.protocol.packet.ClientStatus;
import net.md_5.bungee.protocol.packet.Handshake;
import net.md_5.bungee.protocol.packet.KeepAlive;
import net.md_5.bungee.protocol.packet.LoginRequest;
import net.md_5.bungee.protocol.packet.PluginMessage;
import protocolsupport.api.Connection;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6.ClientCommandPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6.HandshakeCachePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6.KeepAlivePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6.LoginRequestServerHandshakePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6.PluginMessagePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6.ToServerChatPacket;
import protocolsupport.protocol.pipeline.version.AbstractPacketEncoder;
import protocolsupport.protocol.storage.NetworkDataCache;

public class ToServerPacketEncoder extends AbstractPacketEncoder {

	{
		registry.register(Handshake.class, HandshakeCachePacket.class);
		registry.register(LoginRequest.class, LoginRequestServerHandshakePacket.class);
		registry.register(KeepAlive.class, KeepAlivePacket.class);
		registry.register(Chat.class, ToServerChatPacket.class);
		registry.register(PluginMessage.class, PluginMessagePacket.class);
		registry.register(ClientStatus.class, ClientCommandPacket.class);
	}

	public ToServerPacketEncoder(Connection connection, NetworkDataCache cache) {
		super(connection, cache);
	}

}
