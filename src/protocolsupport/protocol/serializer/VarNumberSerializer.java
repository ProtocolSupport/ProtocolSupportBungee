package protocolsupport.protocol.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;

public class VarNumberSerializer {

	public static final int MAX_LENGTH = 5;

	public static int readVarInt(ByteBuf from) {
		int value = 0;
		int length = 0;
		byte part;
		do {
			part = from.readByte();
			value |= (part & 0x7F) << (length++ * 7);
			if (length > 5) {
				throw new DecoderException("VarInt too big");
			}
		} while (part < 0);
		return value;
	}

	public static void writeVarInt(ByteBuf to, int i) {
		while ((i & 0xFFFFFF80) != 0x0) {
			to.writeByte(i | 0x80);
			i >>>= 7;
		}
		to.writeByte(i);
	}

	public static void writeFixedSizeVarInt(ByteBuf to, int i) {
		int writerIndex = to.writerIndex();
		while ((i & 0xFFFFFF80) != 0x0) {
			to.writeByte(i | 0x80);
			i >>>= 7;
		}
		int paddingBytes = MAX_LENGTH - (to.writerIndex() - writerIndex) - 1;
		if (paddingBytes == 0) {
			to.writeByte(i);
		} else {
			to.writeByte(i | 0x80);
			while (--paddingBytes > 0) {
				to.writeByte(0x80);
			}
			to.writeByte(0);
		}
	}
}
