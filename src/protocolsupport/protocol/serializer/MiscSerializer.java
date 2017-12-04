package protocolsupport.protocol.serializer;

import java.text.MessageFormat;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;

public class MiscSerializer {

	public static void writeLFloat(ByteBuf to, float f) {
		to.writeIntLE(Float.floatToIntBits(f));
	}

	public static float readLFloat(ByteBuf from) {
		return Float.intBitsToFloat(from.readIntLE());
	}

	public static byte[] readAllBytes(ByteBuf buf) {
		return MiscSerializer.readBytes(buf, buf.readableBytes());
	}

	public static byte[] readBytes(ByteBuf buf, int length) {
		byte[] result = new byte[length];
		buf.readBytes(result);
		return result;
	}

	protected static void checkLimit(int length, int limit) {
		if (length > limit) {
			throw new DecoderException(MessageFormat.format("Size {0} is bigger than allowed {1}", length, limit));
		}
	}

}
