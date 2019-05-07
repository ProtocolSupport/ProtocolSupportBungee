package protocolsupport.protocol.pipeline.version.v_pe;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.Protocol;
import protocolsupport.api.Connection;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middle.ReadableMiddlePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_pe.CustomEventPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_pe.FromServerChatPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_pe.KickPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_pe.LoginPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_pe.RespawnPacket;
import protocolsupport.protocol.serializer.PEPacketIdSerializer;
import protocolsupport.protocol.storage.NetworkDataCache;
import protocolsupport.protocol.utils.ProtocolVersionsHelper;
import protocolsupport.protocol.utils.registry.PacketIdMiddleTransformerRegistry;

public class FromServerPacketDecoder extends MinecraftDecoder {

	protected final PacketIdMiddleTransformerRegistry<ReadableMiddlePacket> registry = new PacketIdMiddleTransformerRegistry<>();
	{
		registry.register(Protocol.GAME, PEPacketId.Clientbound.PLAY_KICK, KickPacket.class);
		registry.register(Protocol.GAME, PEPacketId.Clientbound.PLAY_START_GAME, LoginPacket.class);
		registry.register(Protocol.GAME, PEPacketId.Dualbound.PLAY_CHAT, FromServerChatPacket.class);
		registry.register(Protocol.GAME, PEPacketId.Clientbound.PLAY_RESPAWN, RespawnPacket.class);
		registry.register(Protocol.GAME, PEPacketId.Dualbound.CUSTOM_EVENT, CustomEventPacket.class);
		//TODO: implement at bungee level (without this entry it's a direct passthrough, so entries will duplicate upon server switch)
		//registry.register(Protocol.GAME, PlayerListItemPacket.PACKET_ID, PlayerListItemPacket.class);
	}

	protected final Connection connection;
	protected final NetworkDataCache cache;

	public FromServerPacketDecoder(Connection connection, NetworkDataCache cache) {
		super(Protocol.GAME, false, ProtocolVersionsHelper.LATEST_PC.getId());
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
		int packetId = PEPacketIdSerializer.readPacketId(buf);
		ReadableMiddlePacket transformer = registry.getTransformer(Protocol.GAME, packetId, false);
		if (transformer == null) {
			buf.resetReaderIndex();
			packets.add(new PacketWrapper(new NoopDefinedPacket(), buf.copy()));
		} else {
			transformer.read(buf);
			if (buf.isReadable()) {
				throw new DecoderException("Did not read all data from packet " + transformer.getClass().getName() + ", bytes left: " + buf.readableBytes());
			}
			packets.addAll(transformer.toNative());
		}
	}

}
