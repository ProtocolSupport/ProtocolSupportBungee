package protocolsupport.protocol.serializer;

import io.netty.buffer.ByteBuf;

public class PEPacketIdSerializer {

	public static int readPacketId(ByteBuf from) {
		int id = VarNumberSerializer.readVarInt(from);
		from.readByte();
		from.readByte();
		return id;
	}

	public static void writePacketId(ByteBuf data, int packetId) {
		VarNumberSerializer.writeVarInt(data, packetId);
		data.writeByte(0);
		data.writeByte(0);
	}

}
