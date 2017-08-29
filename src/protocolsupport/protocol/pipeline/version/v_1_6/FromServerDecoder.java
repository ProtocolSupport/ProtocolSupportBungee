package protocolsupport.protocol.pipeline.version.v_1_6;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.Protocol;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.packet.middle.ReadableMiddlePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.ChatPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.KeepAlivePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.KickPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.LoginPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.PlayerListItemPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.PluginMessagePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.RespawnPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.ScoreboardDisplayPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.ScoreboardObjectivePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.ScoreboardScorePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.ScoreboardTeamPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_6.TabCompleteResponsePacket;
import protocolsupport.protocol.storage.NetworkDataCache;
import protocolsupport.protocol.utils.registry.PacketIdMiddleTransformerRegistry;

public class FromServerDecoder extends MinecraftDecoder {

	protected final PacketIdMiddleTransformerRegistry<ReadableMiddlePacket> registry = new PacketIdMiddleTransformerRegistry<>();
	{
		registry.register(Protocol.GAME, KeepAlivePacket.PACKET_ID, KeepAlivePacket.class);
		registry.register(Protocol.GAME, KickPacket.PACKET_ID, KickPacket.class);
		registry.register(Protocol.GAME, LoginPacket.PACKET_ID, LoginPacket.class);
		registry.register(Protocol.GAME, ChatPacket.PACKET_ID, ChatPacket.class);
		registry.register(Protocol.GAME, RespawnPacket.PACKET_ID, RespawnPacket.class);
		registry.register(Protocol.GAME, PlayerListItemPacket.PACKET_ID, PlayerListItemPacket.class);
		registry.register(Protocol.GAME, TabCompleteResponsePacket.PACKET_ID, TabCompleteResponsePacket.class);
		registry.register(Protocol.GAME, ScoreboardObjectivePacket.PACKET_ID, ScoreboardObjectivePacket.class);
		registry.register(Protocol.GAME, ScoreboardScorePacket.PACKET_ID, ScoreboardScorePacket.class);
		registry.register(Protocol.GAME, ScoreboardDisplayPacket.PACKET_ID, ScoreboardDisplayPacket.class);
		registry.register(Protocol.GAME, ScoreboardTeamPacket.PACKET_ID, ScoreboardTeamPacket.class);
		registry.register(Protocol.GAME, PluginMessagePacket.PACKET_ID, PluginMessagePacket.class);
	}

	protected final Connection connection;
	protected final NetworkDataCache cache;

	public FromServerDecoder(Connection connection, NetworkDataCache cache) {
		super(Protocol.GAME, false, ProtocolVersion.MINECRAFT_1_7_10.getId());
		this.connection = connection;
		this.cache = cache;
		registry.setCallBack(transformer -> {
			transformer.setConnection(this.connection);
			transformer.setSharedStorage(this.cache);
		});
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> packets) throws Exception {
		if (!buf.isReadable()) {
			return;
		}
		buf.markReaderIndex();
		ReadableMiddlePacket transformer = registry.getTransformer(Protocol.GAME, buf.readUnsignedByte(), false);
		if (transformer != null) {
			transformer.read(buf);
			packets.addAll(transformer.toNative());
		} else {
			buf.resetReaderIndex();
			packets.add(new PacketWrapper(null, buf.copy()));
		}
	}

}
