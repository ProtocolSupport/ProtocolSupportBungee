package protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class PacketDataSerializer {

	public static String readString(ByteBuf buf) {
		int length = buf.readUnsignedShort();
		return buf.readBytes(length * 2).toString(StandardCharsets.UTF_16BE);
	}

	public static void writeString(String string, ByteBuf buf) {
		buf.writeShort(string.length());
		buf.writeBytes(string.getBytes(StandardCharsets.UTF_16BE));
	}

	public static ByteBuf readItemStackData(ByteBuf buf) {
		ByteBuf itemdata = Unpooled.buffer();
		int itemId = buf.readShort();
		itemdata.writeShort(itemId);
		if (itemId != -1) {
			itemdata.writeByte(buf.readByte());
			itemdata.writeShort(buf.readShort());
			int nbtlength = buf.readShort();
			itemdata.writeShort(nbtlength);
			if (nbtlength != -1) {
				itemdata.writeBytes(buf.readBytes(nbtlength).array());
			}
		}
		return itemdata.readBytes(itemdata.readableBytes());
	}

	public static ByteBuf readDatawatcherData(ByteBuf buf) {
		ByteBuf datawatcherdata = Unpooled.buffer();
		do {
			final int b0 = buf.readUnsignedByte();
			datawatcherdata.writeByte(b0);
			if (b0 == 127) {
				break;
			}
			final int type = ((b0 & 0xE0) >> 5);
			switch (type) {
				case 0: {
					datawatcherdata.writeByte(buf.readByte());
					break;
				}
				case 1: {
					datawatcherdata.writeShort(buf.readShort());
					break;
				}
				case 2: {
					datawatcherdata.writeInt(buf.readInt());
					break;
				}
				case 3: {
					datawatcherdata.writeFloat(buf.readFloat());
					break;
				}
				case 4: {
					PacketDataSerializer.writeString(PacketDataSerializer.readString(buf), datawatcherdata);
					break;
				}
				case 5: {
					datawatcherdata.writeBytes(PacketDataSerializer.readItemStackData(buf).array());
					break;
				}
				case 6: {
					datawatcherdata.writeInt(buf.readInt());
					datawatcherdata.writeInt(buf.readInt());
					datawatcherdata.writeInt(buf.readInt());
					break;
				}
			}
		} while (true);
		return datawatcherdata.readBytes(datawatcherdata.readableBytes());
	}

	public static byte[] readArray(ByteBuf buf) {
		return buf.readBytes(buf.readShort()).array();
	}

	public static void writeArray(byte[] array, ByteBuf buf) {
		buf.writeShort(array.length);
		buf.writeBytes(array);
	}

}
