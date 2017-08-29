package protocolsupport.protocol.serializer;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import protocolsupport.utils.Utils;

public class LegacySerializer {

	public static String readString(ByteBuf buf) {
		return new String(Utils.readBytes(buf, buf.readUnsignedShort() * 2), StandardCharsets.UTF_16BE);
	}

	public static void writeString(ByteBuf buf, String string) {
		buf.writeShort(string.length());
		buf.writeBytes(string.getBytes(StandardCharsets.UTF_16BE));
	}

	public static byte[] readArray(ByteBuf buf) {
		return Utils.readBytes(buf, buf.readShort());
	}

	public static void writeArray(ByteBuf buf, byte[] array) {
		buf.writeShort(array.length);
		buf.writeBytes(array);
	}

}
