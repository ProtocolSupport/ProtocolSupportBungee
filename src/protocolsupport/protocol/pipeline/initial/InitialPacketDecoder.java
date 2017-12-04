package protocolsupport.protocol.pipeline.initial;

import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import net.md_5.bungee.netty.PipelineUtils;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.pipeline.ChannelHandlers;
import protocolsupport.protocol.pipeline.IPipeLineBuilder;
import protocolsupport.protocol.pipeline.common.PacketCompressor;
import protocolsupport.protocol.pipeline.common.PacketDecompressor;
import protocolsupport.protocol.pipeline.common.VarIntFrameDecoder;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.utils.EncapsulatedProtocolInfo;
import protocolsupport.protocol.utils.EncapsulatedProtocolUtils;
import protocolsupport.utils.Utils;
import protocolsupport.utils.netty.ReplayingDecoderBuffer;
import protocolsupport.utils.netty.ReplayingDecoderBuffer.EOFSignal;


public class InitialPacketDecoder extends SimpleChannelInboundHandler<ByteBuf> {

	private static final int ping152delay = Utils.getJavaPropertyValue("ping152delay", 100, Integer::parseInt);
	private static final int pingLegacyDelay = Utils.getJavaPropertyValue("pinglegacydelay", 200, Integer::parseInt);

	protected final ByteBuf receivedData = Unpooled.buffer();
	protected final ReplayingDecoderBuffer replayingBuffer = new ReplayingDecoderBuffer(receivedData);

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
	public void channelRead0(ChannelHandlerContext ctx, ByteBuf buf)  {
		if (!buf.isReadable()) {
			return;
		}
		receivedData.writeBytes(buf);
		receivedData.readerIndex(0);
		decode(ctx);
	}

	private void decode(ChannelHandlerContext ctx) {
		cancelTask();
		Channel channel = ctx.channel();
		int firstbyte = replayingBuffer.readUnsignedByte();
		try {
			switch (firstbyte) {
				case 0x00: { // encapsulated protocol handsake
					setEncapsulatedProtocol(channel, EncapsulatedProtocolUtils.readInfo(replayingBuffer));
					break;
				}
				case 0xFE: { //old ping or a part of varint length
					if (replayingBuffer.readableBytes() == 0) {
						//no more data received, it may be old protocol, or we just not received all data yet, so delay assuming as really old protocol for some time
						scheduleTask(ctx, new SetProtocolTask(this, channel, ProtocolVersion.MINECRAFT_LEGACY), pingLegacyDelay, TimeUnit.MILLISECONDS);
					} else if (replayingBuffer.readUnsignedByte() == 1) {
						//1.5-1.6 ping or maybe a finishing byte for 1.7+ packet length
						if (replayingBuffer.readableBytes() == 0) {
							//no more data received, it may be 1.5.2 or we just didn't receive 1.6 or 1.7+ data yet, so delay assuming as 1.5.2 for some time
							scheduleTask(ctx, new SetProtocolTask(this, channel, ProtocolVersion.MINECRAFT_1_5_2), ping152delay, TimeUnit.MILLISECONDS);
						} else if (
							(replayingBuffer.readUnsignedByte() == 0xFA) &&
							"MC|PingHost".equals(StringSerializer.readShortUTF16BEString(replayingBuffer))
						) {
							//definitely 1.6
							replayingBuffer.readUnsignedShort();
							setNativeProtocol(channel, ProtocolUtils.get16PingVersion(replayingBuffer.readUnsignedByte()));
						} else {
							//it was 1.7+ handshake after all
							//hope that there won't be any handshake packet with id 0xFA in future because it will be more difficult to support it
							setNativeProtocol(channel, attemptDecodeNewHandshake(replayingBuffer));
						}
					} else {
						//1.7+ handshake
						setNativeProtocol(channel, attemptDecodeNewHandshake(replayingBuffer));
					}
					break;
				}
				case 0x02: { // <= 1.6.4 handshake
					setNativeProtocol(channel, ProtocolUtils.readOldHandshake(replayingBuffer));
					break;
				}
				default: { // >= 1.7 handshake
					setNativeProtocol(channel, attemptDecodeNewHandshake(replayingBuffer));
					break;
				}
			}
		} catch (EOFSignal ex) {
		}
	}

	private void setEncapsulatedProtocol(Channel channel, EncapsulatedProtocolInfo info) {
		ConnectionImpl connection = prepare(channel, info.getVersion());
		IPipeLineBuilder.BUILDERS.get(info.getVersion()).buildBungeeClientCodec(channel, connection);
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.replace(PipelineUtils.FRAME_DECODER, PipelineUtils.FRAME_DECODER, new VarIntFrameDecoder());
		if (info.hasCompression()) {
			pipeline.addAfter(PipelineUtils.FRAME_DECODER, "decompress", new PacketDecompressor());
			pipeline.addAfter(PipelineUtils.FRAME_PREPENDER, "compress", new PacketCompressor(256));
		}
		channel.pipeline().firstContext().fireChannelRead(receivedData);
	}

	protected void setNativeProtocol(Channel channel, ProtocolVersion version) {
		ConnectionImpl connection = prepare(channel, version);
		IPipeLineBuilder builder = IPipeLineBuilder.BUILDERS.get(version);
		if (builder != null) {
			builder.buildBungeeClientCodec(channel, connection);
			builder.buildBungeeClientPipeLine(channel, connection);
		}
		receivedData.readerIndex(0);
		channel.pipeline().firstContext().fireChannelRead(receivedData);
	}

	protected ConnectionImpl prepare(Channel channel, ProtocolVersion version) {
		channel.pipeline().remove(ChannelHandlers.INITIAL_DECODER);
		ConnectionImpl connection = ConnectionImpl.getFromChannel(channel);
		connection.setVersion(version);
		return connection;
	}

	private static ProtocolVersion attemptDecodeNewHandshake(ByteBuf bytebuf) {
		bytebuf.readerIndex(0);
		return ProtocolUtils.readNewHandshake(bytebuf.readSlice(VarNumberSerializer.readVarInt(bytebuf)));
	}

}
