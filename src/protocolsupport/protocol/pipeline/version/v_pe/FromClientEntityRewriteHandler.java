package protocolsupport.protocol.pipeline.version.v_pe;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import protocolsupport.api.Connection;
import protocolsupport.protocol.storage.NetworkDataCache;

public class FromClientEntityRewriteHandler extends MessageToMessageDecoder<ByteBuf> {

	protected final Connection connection;
	protected final NetworkDataCache cache;

	public FromClientEntityRewriteHandler(Connection connection, NetworkDataCache cache) {
		this.connection = connection;
		this.cache = cache;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
		out.add(buf.retain());
	}

}
