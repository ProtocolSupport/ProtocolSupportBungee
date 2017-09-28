package protocolsupport.protocol.pipeline.version.v_1_6;

import net.md_5.bungee.protocol.Protocol;
import protocolsupport.api.Connection;
import protocolsupport.protocol.packet.middleimpl.readable.handshake.v_4_5_6.LoginHandshakePacket;
import protocolsupport.protocol.packet.middleimpl.readable.handshake.v_4_5_6.PingHandshakePacket;
import protocolsupport.protocol.packet.middleimpl.readable.login.v_4_5_6.EncryptionResponsePacket;
import protocolsupport.protocol.packet.middleimpl.readable.login.v_4_5_6.LoginClientCommandPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5.EntityActionPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5.PlayerAbilitiesPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.AnimationPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.BlockDigPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.BlockPlacePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.ClientCommandPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.ClientSettingsPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.CreativeSetSlotPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.FromClientChatPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.HeldSlotPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.InventoryClickPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.InventoryClosePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.InventoryEnchant;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.InventoryTransactionPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.KeepAlivePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.KickPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.PlayerFlyingPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.PlayerLookPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.PlayerPositionLookPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.PlayerPositionPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.PluginMessagePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.TabCompleteRequestPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.UpdateSignPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.UseEntityPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.SteerVehiclePacket;
import protocolsupport.protocol.pipeline.version.AbstractFromClientPacketDecoder;
import protocolsupport.protocol.storage.NetworkDataCache;

public class FromClientPacketDecoder extends AbstractFromClientPacketDecoder {

	{
		registry.register(Protocol.HANDSHAKE, LoginHandshakePacket.PACKET_ID, LoginHandshakePacket.class);
		registry.register(Protocol.HANDSHAKE, PingHandshakePacket.PACKET_ID, PingHandshakePacket.class);
		registry.register(Protocol.LOGIN, EncryptionResponsePacket.PACKET_ID, EncryptionResponsePacket.class);
		registry.register(Protocol.LOGIN, LoginClientCommandPacket.PACKET_ID, LoginClientCommandPacket.class);
		registry.register(Protocol.GAME, KeepAlivePacket.PACKET_ID, KeepAlivePacket.class);
		registry.register(Protocol.GAME, FromClientChatPacket.PACKET_ID, FromClientChatPacket.class);
		registry.register(Protocol.GAME, UseEntityPacket.PACKET_ID, UseEntityPacket.class);
		registry.register(Protocol.GAME, PlayerFlyingPacket.PACKET_ID, PlayerFlyingPacket.class);
		registry.register(Protocol.GAME, PlayerPositionPacket.PACKET_ID, PlayerPositionPacket.class);
		registry.register(Protocol.GAME, PlayerLookPacket.PACKET_ID, PlayerLookPacket.class);
		registry.register(Protocol.GAME, PlayerPositionLookPacket.PACKET_ID, PlayerPositionLookPacket.class);
		registry.register(Protocol.GAME, BlockDigPacket.PACKET_ID, BlockDigPacket.class);
		registry.register(Protocol.GAME, BlockPlacePacket.PACKET_ID, BlockPlacePacket.class);
		registry.register(Protocol.GAME, HeldSlotPacket.PACKET_ID, HeldSlotPacket.class);
		registry.register(Protocol.GAME, AnimationPacket.PACKET_ID, AnimationPacket.class);
		registry.register(Protocol.GAME, EntityActionPacket.PACKET_ID, EntityActionPacket.class);
		registry.register(Protocol.GAME, SteerVehiclePacket.PACKET_ID, SteerVehiclePacket.class);
		registry.register(Protocol.GAME, InventoryClosePacket.PACKET_ID, InventoryClosePacket.class);
		registry.register(Protocol.GAME, InventoryClickPacket.PACKET_ID, InventoryClickPacket.class);
		registry.register(Protocol.GAME, InventoryTransactionPacket.PACKET_ID, InventoryTransactionPacket.class);
		registry.register(Protocol.GAME, CreativeSetSlotPacket.PACKET_ID, CreativeSetSlotPacket.class);
		registry.register(Protocol.GAME, InventoryEnchant.PACKET_ID, InventoryEnchant.class);
		registry.register(Protocol.GAME, UpdateSignPacket.PACKET_ID, UpdateSignPacket.class);
		registry.register(Protocol.GAME, TabCompleteRequestPacket.PACKET_ID, TabCompleteRequestPacket.class);
		registry.register(Protocol.GAME, PlayerAbilitiesPacket.PACKET_ID, PlayerAbilitiesPacket.class);
		registry.register(Protocol.GAME, ClientSettingsPacket.PACKET_ID, ClientSettingsPacket.class);
		registry.register(Protocol.GAME, ClientCommandPacket.PACKET_ID, ClientCommandPacket.class);
		registry.register(Protocol.GAME, PluginMessagePacket.PACKET_ID, PluginMessagePacket.class);
		registry.register(Protocol.GAME, KickPacket.PACKET_ID, KickPacket.class);
	}

	public FromClientPacketDecoder(Connection connection, NetworkDataCache cache) {
		super(connection, cache);
	}

}
