package protocolsupport.protocol.pipeline.initial;

import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderException;
import io.netty.util.concurrent.Future;
import net.md_5.bungee.netty.PipelineUtils;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.injector.pe.PEProxyServerInfoHandler;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.pipeline.ChannelHandlers;
import protocolsupport.protocol.pipeline.IPipeLineBuilder;
import protocolsupport.protocol.pipeline.common.PacketCompressor;
import protocolsupport.protocol.pipeline.common.PacketDecompressor;
import protocolsupport.protocol.pipeline.common.VarIntFrameDecoder;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.utils.EncapsulatedProtocolInfo;
import protocolsupport.protocol.utils.EncapsulatedProtocolUtils;
import protocolsupport.utils.Utils;
import protocolsupport.utils.netty.Decompressor;
import protocolsupport.utils.netty.ReplayingDecoderBuffer;
import protocolsupport.utils.netty.ReplayingDecoderBuffer.EOFSignal;


public class InitialPacketDecoder extends SimpleChannelInboundHandler<ByteBuf> {

	private static final int ping152delay = Utils.getJavaPropertyValue("ping152delay", 100, Integer::parseInt);
	private static final int pingLegacyDelay = Utils.getJavaPropertyValue("pinglegacydelay", 200, Integer::parseInt);

	protected final ReplayingDecoderBuffer buffer = new ReplayingDecoderBuffer(Unpooled.buffer());

	protected Future<?> responseTask;

	protected void scheduleTask(ChannelHandlerContext ctx, Runnable task, long delay, TimeUnit tu) {
		responseTask = ctx.executor().schedule(task, delay, tu);
	}

	protected void cancelTask() {
		if (responseTask != null) {
			responseTask.cancel(true);
			responseTask = null;
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		cancelTask();
		super.channelInactive(ctx);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		cancelTask();
		super.handlerRemoved(ctx);
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
		if (!buf.isReadable()) {
			return;
		}
		buffer.writeBytes(buf);
		buffer.readerIndex(0);
		decode(ctx);
	}


	private boolean firstread = true;
	private EncapsulatedProtocolInfo encapsulatedinfo = null;

	private void decode(ChannelHandlerContext ctx) throws Exception {
		cancelTask();
		if (firstread) {
			int firstbyte = buffer.readUnsignedByte();
			if (firstbyte == EncapsulatedProtocolUtils.FIRST_BYTE) {
				encapsulatedinfo = EncapsulatedProtocolUtils.readInfo(buffer);
				buffer.discardReadBytes();
			}
			buffer.readerIndex(0);
			firstread = false;
		}
		try {
			if (encapsulatedinfo == null) {
				decodeRaw(ctx);
			} else {
				decodeEncapsulated(ctx);
			}
		} catch (EOFSignal ex) {
		}
	}

	private void decodeRaw(ChannelHandlerContext ctx) {
		Channel channel = ctx.channel();
		int firstbyte = buffer.readUnsignedByte();
		switch (firstbyte) {
			case 0xFE: { //old ping or a part of varint length
				if (buffer.readableBytes() == 0) {
					//no more data received, it may be old protocol, or we just not received all data yet, so delay assuming as really old protocol for some time
					scheduleTask(ctx, new SetProtocolTask(this, channel, ProtocolVersion.MINECRAFT_LEGACY), pingLegacyDelay, TimeUnit.MILLISECONDS);
				} else if (buffer.readUnsignedByte() == 1) {
					//1.5-1.6 ping or maybe a finishing byte for 1.7+ packet length
					if (buffer.readableBytes() == 0) {
						//no more data received, it may be 1.5.2 or we just didn't receive 1.6 or 1.7+ data yet, so delay assuming as 1.5.2 for some time
						scheduleTask(ctx, new SetProtocolTask(this, channel, ProtocolVersion.MINECRAFT_1_5_2), ping152delay, TimeUnit.MILLISECONDS);
					} else if (
						(buffer.readUnsignedByte() == 0xFA) &&
						"MC|PingHost".equals(StringSerializer.readShortUTF16BEString(buffer))
					) {
						//definitely 1.6
						buffer.readUnsignedShort();
						setProtocol(channel, ProtocolUtils.get16PingVersion(buffer.readUnsignedByte()));
					} else {
						//it was 1.7+ handshake after all
						//hope that there won't be any handshake packet with id 0xFA in future because it will be more difficult to support it
						setProtocol(channel, attemptDecodeNewHandshake(buffer));
					}
				} else {
					//1.7+ handshake
					setProtocol(channel, attemptDecodeNewHandshake(buffer));
				}
				break;
			}
			case 0x02: { // <= 1.6.4 handshake
				setProtocol(channel, ProtocolUtils.readOldHandshake(buffer));
				break;
			}
			default: { // >= 1.7 handshake
				setProtocol(channel, attemptDecodeNewHandshake(buffer));
				break;
			}
		}
	}

	private static ProtocolVersion attemptDecodeNewHandshake(ByteBuf bytebuf) {
		bytebuf.readerIndex(0);
		return ProtocolUtils.readNewHandshake(bytebuf.readSlice(VarNumberSerializer.readVarInt(bytebuf)));
	}

	private void decodeEncapsulated(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		ByteBuf firstpacketdata = buffer.readSlice(VarNumberSerializer.readVarInt(buffer));
		if (encapsulatedinfo.hasCompression()) {
			int uncompressedlength = VarNumberSerializer.readVarInt(firstpacketdata);
			if (uncompressedlength != 0) {
				firstpacketdata = Unpooled.wrappedBuffer(Decompressor.decompressStatic(MiscSerializer.readAllBytes(firstpacketdata), uncompressedlength));
			}
		}
		int firstbyte = firstpacketdata.readUnsignedByte();
		switch (firstbyte) {
			case 0xFE: { //legacy ping
				if (firstpacketdata.readableBytes() == 0) {
					//< 1.5.2
					setProtocol(channel, ProtocolVersion.MINECRAFT_LEGACY);
				} else if (firstpacketdata.readUnsignedByte() == 1) {
					//1.5 or 1.6 ping
					if (firstpacketdata.readableBytes() == 0) {
						//1.5.*, we just assume 1.5.2
						setProtocol(channel, ProtocolVersion.MINECRAFT_1_5_2);
					} else if (
						(firstpacketdata.readUnsignedByte() == 0xFA) &&
						"MC|PingHost".equals(StringSerializer.readShortUTF16BEString(firstpacketdata))
					) {
						//1.6.*
						firstpacketdata.readUnsignedShort();
						setProtocol(channel, ProtocolUtils.get16PingVersion(firstpacketdata.readUnsignedByte()));
					} else {
						throw new DecoderException("Unable to detect incoming protocol");
					}
				} else {
					throw new DecoderException("Unable to detect incoming protocol");
				}
				break;
			}
			case 0x02: { // <= 1.6.4 handshake
				setProtocol(channel, ProtocolUtils.readOldHandshake(firstpacketdata));
				break;
			}
			case 0x00: { // >= 1.7 handshake
				setProtocol(channel, ProtocolUtils.readNewHandshake(firstpacketdata));
				break;
			}
			case PEPacketId.Serverbound.HANDSHAKE_LOGIN: {
				setProtocol(channel, ProtocolUtils.readPEHandshake(firstpacketdata));
				break;
			}
			case PEProxyServerInfoHandler.PACKET_ID: {
				setProtocol(channel, ProtocolVersion.MINECRAFT_PE);
				break;
			}
			default: {
				throw new DecoderException("Unable to detect incoming protocol");
			}
		}
	}

	protected void setProtocol(Channel channel, ProtocolVersion version) {
		ConnectionImpl connection = prepare(channel, version);
		IPipeLineBuilder builder = IPipeLineBuilder.BUILDERS.get(connection.getVersion());
		builder.buildBungeeClientCodec(channel, connection);
		if (encapsulatedinfo == null) {
			builder.buildBungeeClientPipeLine(channel, connection);
		} else {
			ChannelPipeline pipeline = channel.pipeline();
			pipeline.replace(PipelineUtils.FRAME_DECODER, PipelineUtils.FRAME_DECODER, new VarIntFrameDecoder());
			if (encapsulatedinfo.hasCompression()) {
				pipeline.addAfter(PipelineUtils.FRAME_DECODER, "decompress", new PacketDecompressor());
				pipeline.addAfter(PipelineUtils.FRAME_PREPENDER, "compress", new PacketCompressor(256));
			}
			if ((encapsulatedinfo.getAddress() != null) && connection.getRawAddress().getAddress().isLoopbackAddress()) {
				connection.changeAddress(encapsulatedinfo.getAddress());
			}
		}
		buffer.readerIndex(0);
		channel.pipeline().firstContext().fireChannelRead(buffer.unwrap());
	}

	protected static ConnectionImpl prepare(Channel channel, ProtocolVersion version) {
		channel.pipeline().remove(ChannelHandlers.INITIAL_DECODER);
		ConnectionImpl connection = ConnectionImpl.getFromChannel(channel);
		connection.setVersion(version);
		return connection;
	}

}
