package protocolsupport.protocol.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import protocolsupport.protocol.serializer.VarNumberSerializer;

public class EncapsulatedProtocolUtils {

	public static ByteBuf createHandshake(boolean hasCompression, int protocoltype, int protocolversion) {
		ByteBuf data = Unpooled.buffer();
		data.writeByte(0);
		VarNumberSerializer.writeVarInt(data, 0);
		data.writeBoolean(false);
		data.writeBoolean(hasCompression);
		VarNumberSerializer.writeVarInt(data, protocoltype);
		VarNumberSerializer.writeVarInt(data, protocolversion);
		return data;
	}

}
