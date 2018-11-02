package protocolsupport.protocol.pipeline.version.v_pe;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.Protocol;
import protocolsupport.api.Connection;
import protocolsupport.injector.pe.PEProxyServerInfoHandler;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middle.ReadableMiddlePacket;
import protocolsupport.protocol.packet.middleimpl.readable.handshake.v_pe.LoginHandshakePacket;
import protocolsupport.protocol.packet.middleimpl.readable.handshake.v_pe.PingHandshakePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_pe.CommandRequestPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_pe.FromClientChatPacket;
import protocolsupport.protocol.serializer.PEPacketIdSerializer;
import protocolsupport.protocol.storage.NetworkDataCache;
import protocolsupport.protocol.utils.ProtocolVersionsHelper;
import protocolsupport.protocol.utils.registry.PacketIdMiddleTransformerRegistry;

public class FromClientPacketDecoder extends MinecraftDecoder {

	protected final PacketIdMiddleTransformerRegistry<ReadableMiddlePacket> registry = new PacketIdMiddleTransformerRegistry<>();
	{
		registry.register(Protocol.HANDSHAKE, PEPacketId.Serverbound.HANDSHAKE_LOGIN, LoginHandshakePacket.class);
		registry.register(Protocol.HANDSHAKE, PEProxyServerInfoHandler.PACKET_ID, PingHandshakePacket.class);
		registry.register(Protocol.GAME, PEPacketId.Dualbound.PLAY_CHAT, FromClientChatPacket.class);
		registry.register(Protocol.GAME, PEPacketId.Serverbound.PLAY_COMMAND_REQEST, CommandRequestPacket.class);
	}

	protected final Connection connection;
	protected final NetworkDataCache cache;

	protected Protocol protocol = Protocol.HANDSHAKE;

	public FromClientPacketDecoder(Connection connection, NetworkDataCache cache) {
		super(Protocol.HANDSHAKE, true, ProtocolVersionsHelper.LATEST_PC.getId());
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

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> packets) throws Exception {
		if (!buf.isReadable()) {
			return;
		}
		buf.markReaderIndex();
		int packetId = PEPacketIdSerializer.readPacketId(buf);
		//System.out.println("FROM CLIENT: " + packetId);
		ReadableMiddlePacket transformer = registry.getTransformer(protocol, packetId, false);
		if (transformer == null) {
			//System.out.println("Couldn't find any transformer for packet " + packetId + ", adding null to the packet list...");
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
