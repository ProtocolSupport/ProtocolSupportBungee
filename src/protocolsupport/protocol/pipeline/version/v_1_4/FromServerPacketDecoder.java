package protocolsupport.protocol.pipeline.version.v_1_4;

import net.md_5.bungee.protocol.Protocol;
import protocolsupport.api.Connection;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.FromServerChatPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.KeepAlivePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.KickPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.LoginPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.PlayerListItemPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.PluginMessagePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.RespawnPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.TabCompleteResponsePacket;
import protocolsupport.protocol.pipeline.version.AbstractFromServerPacketDecoder;
import protocolsupport.protocol.storage.NetworkDataCache;

public class FromServerPacketDecoder extends AbstractFromServerPacketDecoder {

	{
		registry.register(Protocol.GAME, KeepAlivePacket.PACKET_ID, KeepAlivePacket.class);
		registry.register(Protocol.GAME, KickPacket.PACKET_ID, KickPacket.class);
		registry.register(Protocol.GAME, LoginPacket.PACKET_ID, LoginPacket.class);
		registry.register(Protocol.GAME, FromServerChatPacket.PACKET_ID, FromServerChatPacket.class);
		registry.register(Protocol.GAME, RespawnPacket.PACKET_ID, RespawnPacket.class);
		registry.register(Protocol.GAME, PlayerListItemPacket.PACKET_ID, PlayerListItemPacket.class);
		registry.register(Protocol.GAME, TabCompleteResponsePacket.PACKET_ID, TabCompleteResponsePacket.class);
		registry.register(Protocol.GAME, PluginMessagePacket.PACKET_ID, PluginMessagePacket.class);
	}

	public FromServerPacketDecoder(Connection connection, NetworkDataCache cache) {
		super(connection, cache);
	}

}
