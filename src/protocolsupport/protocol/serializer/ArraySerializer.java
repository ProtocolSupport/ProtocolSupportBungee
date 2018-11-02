package protocolsupport.protocol.serializer;

import io.netty.buffer.ByteBuf;
import protocolsupport.utils.Utils;

public class ArraySerializer {

	public static byte[] readVarIntLengthByteArray(ByteBuf from) {
		return MiscSerializer.readBytes(from, VarNumberSerializer.readVarInt(from));
	}

	public static byte[] readShortLengthByteArray(ByteBuf to) {
		return Utils.readBytes(to, to.readShort());
	}

	public static void writeShortLengthByteArray(ByteBuf to, byte[] array) {
		to.writeShort(array.length);
		to.writeBytes(array);
	}

	public static void writeVarIntLengthByteArray(ByteBuf to, ByteBuf array) {
		VarNumberSerializer.writeVarInt(to, array.readableBytes());
		to.writeBytes(array);
	}

	public static byte[] readVarIntByteArray(ByteBuf from) {
		return MiscSerializer.readBytes(from, VarNumberSerializer.readVarInt(from));
	}

	public static ByteBuf readVarIntByteArraySlice(ByteBuf from, int limit) {
		int length = VarNumberSerializer.readVarInt(from);
		MiscSerializer.checkLimit(length, limit);
		return from.readSlice(length);
	}

	public static ByteBuf readVarIntByteArraySlice(ByteBuf from) {
		return from.readSlice(VarNumberSerializer.readVarInt(from));
	}

}
