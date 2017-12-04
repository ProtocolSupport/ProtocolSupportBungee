package protocolsupport.protocol.serializer;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import protocolsupport.utils.Utils;

public class StringSerializer {

	public static String readVarIntUTF8String(ByteBuf from) {
		return new String(MiscSerializer.readBytes(from, VarNumberSerializer.readVarInt(from)), StandardCharsets.UTF_8);
	}

	public static void writeVarIntUTF8String(ByteBuf to, String string) {
		byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
		VarNumberSerializer.writeVarInt(to, bytes.length);
		to.writeBytes(bytes);
	}

	public static String readShortUTF16BEString(ByteBuf buf) {
		return new String(Utils.readBytes(buf, buf.readUnsignedShort() * 2), StandardCharsets.UTF_16BE);
	}

	public static void writeShortUTF16BEString(ByteBuf buf, String string) {
		buf.writeShort(string.length());
		buf.writeBytes(string.getBytes(StandardCharsets.UTF_16BE));
	}

}
