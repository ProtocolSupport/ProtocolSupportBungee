package protocolsupport.protocol.pipeline.version.v_1_4;

import net.md_5.bungee.protocol.Protocol;
import protocolsupport.api.Connection;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.handshake.v_4_5.PingHandshakePacket;
import protocolsupport.protocol.packet.middleimpl.readable.handshake.v_4_5_6.LoginHandshakePacket;
import protocolsupport.protocol.packet.middleimpl.readable.login.v_4_5_6.EncryptionResponsePacket;
import protocolsupport.protocol.packet.middleimpl.readable.login.v_4_5_6.LoginClientCommandPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5.EntityActionPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5.PlayerAbilitiesPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.AnimationPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.BlockDigPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.BlockPlacePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.ClientCommandPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.ClientSettingsPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.FromClientChatPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.HeldSlotPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.InventoryClickPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.InventoryClosePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.InventoryCreativeSetSlotPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6.InventorySelectEnchant;
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
import protocolsupport.protocol.pipeline.version.LegacyAbstractFromClientPacketDecoder;
import protocolsupport.protocol.storage.NetworkDataCache;

public class FromClientPacketDecoder extends LegacyAbstractFromClientPacketDecoder {

	{
		registry.register(Protocol.HANDSHAKE, LegacyPacketId.Serverbound.HANDSHAKE_LOGIN, LoginHandshakePacket.class);
		registry.register(Protocol.HANDSHAKE, LegacyPacketId.Serverbound.HANDSHAKE_PING, PingHandshakePacket.class);
		registry.register(Protocol.LOGIN, LegacyPacketId.Serverbound.LOGIN_ENCRYPTION_RESPONSE, EncryptionResponsePacket.class);
		registry.register(Protocol.LOGIN, LegacyPacketId.Serverbound.LOGIN_PLAY_CLIENT_COMMAND, LoginClientCommandPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Dualbound.PLAY_KEEP_ALIVE, KeepAlivePacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Dualbound.PLAY_CHAT, FromClientChatPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Serverbound.PLAY_USE_ENTITY, UseEntityPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Serverbound.PLAY_FLYING, PlayerFlyingPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Serverbound.PLAY_POSITION, PlayerPositionPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Serverbound.PLAY_LOOK, PlayerLookPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Serverbound.PLAY_POSITION_LOOK, PlayerPositionLookPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Serverbound.PLAY_BLOCK_DIG, BlockDigPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Serverbound.PLAY_BLOCK_PLACE, BlockPlacePacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Serverbound.PLAY_HELD_SLOT, HeldSlotPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Dualbound.PLAY_ANIMATION, AnimationPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Dualbound.PLAY_ENTITY_ACTION, EntityActionPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Serverbound.PLAY_INVENTORY_CLOSE, InventoryClosePacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Serverbound.PLAY_INVENTORY_CLICK, InventoryClickPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Dualbound.PLAY_INVENTORY_TRANSACTION, InventoryTransactionPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Serverbound.PLAY_INVENTORY_CREATIVE_SET_SLOT, InventoryCreativeSetSlotPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Serverbound.PLAY_INVENTORY_SELECT_ENCHANT, InventorySelectEnchant.class);
		registry.register(Protocol.GAME, LegacyPacketId.Dualbound.PLAY_UPDATE_SIGN, UpdateSignPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Dualbound.PLAY_TAB_COMPLETE, TabCompleteRequestPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Dualbound.PLAY_ABILITIES, PlayerAbilitiesPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Serverbound.PLAY_CLIENT_SETTINGS, ClientSettingsPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Serverbound.LOGIN_PLAY_CLIENT_COMMAND, ClientCommandPacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Dualbound.PLAY_PLUGIN_MESSAGE, PluginMessagePacket.class);
		registry.register(Protocol.GAME, LegacyPacketId.Dualbound.PLAY_KICK, KickPacket.class);
	}

	public FromClientPacketDecoder(Connection connection, NetworkDataCache cache) {
		super(connection, cache);
	}

}
