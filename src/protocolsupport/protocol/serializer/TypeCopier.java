package protocolsupport.protocol.serializer;

import io.netty.buffer.ByteBuf;

public class TypeCopier {

	public static void copyItemStack(ByteBuf from, ByteBuf to) {
		int itemId = from.readShort();
		to.writeShort(itemId);
		if (itemId != -1) {
			to.writeByte(from.readByte());
			to.writeShort(from.readShort());
			int nbtlength = from.readShort();
			to.writeShort(nbtlength);
			if (nbtlength != -1) {
				copyBytes(from, to, nbtlength);
			}
		}
	}

	public static void copyString(ByteBuf from, ByteBuf to) {
		int length = from.readUnsignedShort();
		to.writeShort(length);
		copyBytes(from, to, length * 2);
	}

	public static void copyBytes(ByteBuf from, ByteBuf to, int length) {
		to.writeBytes(MiscSerializer.readBytes(from, length));
	}

}
