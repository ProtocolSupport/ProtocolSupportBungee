package protocolsupport.protocol.pipeline.version.v_1_6;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.Protocol;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.packet.middle.ReadableMiddlePacket;
import protocolsupport.protocol.packet.middleimpl.readable.handshake.v_6.LoginHandshakePacket;
import protocolsupport.protocol.packet.middleimpl.readable.handshake.v_6.PingHandshakePacket;
import protocolsupport.protocol.packet.middleimpl.readable.login.v_6.EncryptionResponsePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.AnimationPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.BlockDigPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.BlockPlacePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.ChatPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.ClientCommandPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.ClientSettingsPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.CreativeSetSlotPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.EntityActionPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.HeldSlotPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.InventoryClickPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.InventoryClosePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.InventoryEnchant;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.InventoryTransactionPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.KeepAlivePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.KickPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.PlayerAbilitiesPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.PlayerFlyingPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.PlayerLookPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.PlayerPositionLookPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.PlayerPositionPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.PluginMessagePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.SteerVehiclePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.TabCompleteRequestPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.UpdateSignPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.UseEntityPacket;
import protocolsupport.protocol.storage.NetworkDataCache;
import protocolsupport.protocol.utils.registry.PacketIdMiddleTransformerRegistry;
import protocolsupport.utils.netty.ReplayingDecoderBuffer;
import protocolsupport.utils.netty.ReplayingDecoderBuffer.EOFSignal;

public class FromClientDecoder extends MinecraftDecoder {

	protected final PacketIdMiddleTransformerRegistry<ReadableMiddlePacket> registry = new PacketIdMiddleTransformerRegistry<>();
	{
		registry.register(Protocol.HANDSHAKE, LoginHandshakePacket.PACKET_ID, LoginHandshakePacket.class);
		registry.register(Protocol.HANDSHAKE, PingHandshakePacket.PACKET_ID, PingHandshakePacket.class);
		registry.register(Protocol.LOGIN, EncryptionResponsePacket.PACKET_ID, EncryptionResponsePacket.class);
		registry.register(Protocol.GAME, KeepAlivePacket.PACKET_ID, KeepAlivePacket.class);
		registry.register(Protocol.GAME, ChatPacket.PACKET_ID, ChatPacket.class);
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

	protected final Connection connection;
	protected final NetworkDataCache cache;

	protected Protocol protocol = Protocol.HANDSHAKE;

	public FromClientDecoder(Connection connection, NetworkDataCache cache) {
		super(Protocol.HANDSHAKE, true, ProtocolVersion.MINECRAFT_1_7_10.getId());
		this.connection = connection;
		this.cache = cache;
		registry.setCallBack(transformer -> {
			transformer.setConnection(this.connection);
			transformer.setSharedStorage(this.cache);
		});
	}

	@Override
	public void setProtocol(Protocol protocol) {
		super.setProtocol(protocol);
		this.protocol = protocol;
	}

	private final ByteBuf internal = Unpooled.buffer();
	private final ReplayingDecoderBuffer replay = new ReplayingDecoderBuffer(internal);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> packets) throws Exception {
		if (!buf.isReadable()) {
			return;
		}
		internal.writeBytes(buf);
		replay.markReaderIndex();
		try {
			while (replay.isReadable()) {
				replay.markReaderIndex();
				ReadableMiddlePacket transformer = registry.getTransformer(protocol, replay.readUnsignedByte(), true);
				transformer.read(replay);
				packets.addAll(transformer.toNative());
				internal.discardSomeReadBytes();
			}
		} catch (EOFSignal e) {
			replay.resetReaderIndex();
		}
	}

}
