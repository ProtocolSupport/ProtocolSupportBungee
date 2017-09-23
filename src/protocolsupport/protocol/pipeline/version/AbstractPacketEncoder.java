package protocolsupport.protocol.pipeline.version;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.EncoderException;
import io.netty.util.ReferenceCountUtil;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.MinecraftEncoder;
import net.md_5.bungee.protocol.Protocol;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.packet.middle.WriteableMiddlePacket;
import protocolsupport.protocol.storage.NetworkDataCache;
import protocolsupport.protocol.utils.registry.ClassMapMiddleTransformerRegistry;

public abstract class AbstractPacketEncoder extends MinecraftEncoder {

	protected final ClassMapMiddleTransformerRegistry<DefinedPacket, WriteableMiddlePacket<?>> registry = new ClassMapMiddleTransformerRegistry<>();

	protected final Connection connection;
	protected final NetworkDataCache cache;

	public AbstractPacketEncoder(Connection connection, NetworkDataCache cache) {
		super(Protocol.HANDSHAKE, true, ProtocolVersion.MINECRAFT_1_7_10.getId());
		this.connection = connection;
		this.cache = cache;
		registry.setCallBack(transformer -> {
			transformer.setConnection(this.connection);
			transformer.setSharedStorage(this.cache);
		});
	}

	@Override
	public void write(final ChannelHandlerContext ctx, final Object msgObject, final ChannelPromise promise) throws Exception {
		try {
			if (acceptOutboundMessage(msgObject)) {
				DefinedPacket msg = (DefinedPacket) msgObject;
				try {
					encode(ctx, msg, null);
				} finally {
					ReferenceCountUtil.release(msg);
				}
			} else {
				ctx.write(msgObject, promise);
			}
		} catch (EncoderException e) {
			throw e;
		} catch (Throwable e2) {
			throw new EncoderException(e2);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void encode(ChannelHandlerContext ctx, DefinedPacket msg, ByteBuf out) throws Exception {
		WriteableMiddlePacket<DefinedPacket> transformer = (WriteableMiddlePacket<DefinedPacket>) registry.getTransformer(msg.getClass());
		transformer.toData(msg).forEach(ctx::writeAndFlush);
	}

}
