package protocolsupport.protocol.pipeline.version.v_pe;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.Protocol;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.packet.middle.ReadableMiddlePacket;
import protocolsupport.protocol.packet.middleimpl.readable.handshake.v_pe.LoginHandshakePacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_pe.CommandRequestPacket;
import protocolsupport.protocol.packet.middleimpl.readable.play.v_pe.FromClientChatPacket;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.storage.NetworkDataCache;
import protocolsupport.protocol.utils.registry.PacketIdMiddleTransformerRegistry;

public class FromClientPacketDecoder extends MinecraftDecoder {

	protected final PacketIdMiddleTransformerRegistry<ReadableMiddlePacket> registry = new PacketIdMiddleTransformerRegistry<>();
	{
		registry.register(Protocol.HANDSHAKE, LoginHandshakePacket.PACKET_ID, LoginHandshakePacket.class);
		registry.register(Protocol.GAME, FromClientChatPacket.PACKET_ID, FromClientChatPacket.class);
		registry.register(Protocol.GAME, CommandRequestPacket.PACKET_ID, CommandRequestPacket.class);
	}

	protected final Connection connection;
	protected final NetworkDataCache cache;

	protected Protocol protocol = Protocol.HANDSHAKE;

	public FromClientPacketDecoder(Connection connection, NetworkDataCache cache) {
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

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> packets) throws Exception {
		if (!buf.isReadable()) {
			return;
		}
		buf.markReaderIndex();
		ReadableMiddlePacket transformer = registry.getTransformer(protocol, readPacketId(buf), false);
		if (transformer == null) {
			buf.resetReaderIndex();
			packets.add(new PacketWrapper(null, buf.copy()));
		} else {
			transformer.read(buf);
			if (buf.isReadable()) {
				throw new DecoderException("Did not read all data from packet " + transformer.getClass().getName() + ", bytes left: " + buf.readableBytes());
			}
			packets.addAll(transformer.toNative());
		}
	}

	protected int readPacketId(ByteBuf from) {
		int id = VarNumberSerializer.readVarInt(from);
		from.readByte();
		from.readByte();
		return id;
	}

}
