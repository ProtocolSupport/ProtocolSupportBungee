package protocolsupport.protocol.packet.middleimpl.readable.play.v_pe;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Chat;
import protocolsupport.protocol.packet.middleimpl.readable.PEDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;

import java.util.Collection;
import java.util.Collections;

public class CommandRequestPacket extends PEDefinedReadableMiddlePacket {

	public static final int PACKET_ID = 77;

	protected String command;
	//private static final int ORIGIN_PLAYER = 0;
	//private static final int ORIGIN_BLOCK = 1;
	//private static final int ORIGIN_MINECART_BLOCK = 2;
	private static final int ORIGIN_DEV_CONSOLE = 3;
	private static final int ORIGIN_TEST = 4;
	//private static final int ORIGIN_AUTOMATION_PLAYER = 5;
	//private static final int ORIGIN_CLIENT_AUTOMATION = 6;
	//private static final int ORIGIN_DEDICATED_SERVER = 7;
	//private static final int ORIGIN_ENTITY = 8;
	//private static final int ORIGIN_VIRTUAL = 9;
	//private static final int ORIGIN_GAME_ARGUMENT = 10;
	//private static final int ORIGIN_ENTITY_SERVER = 11;

	public CommandRequestPacket() {
		super(PACKET_ID);
	}

	@Override
	protected void read0(ByteBuf from) {
		command = StringSerializer.readVarIntUTF8String(from);
		// Command Origin Data
		int type = VarNumberSerializer.readVarInt(from); // type
		MiscSerializer.readUUIDLE(from); // UUID
		StringSerializer.readVarIntUTF8String(from); // request ID
		if (type == ORIGIN_DEV_CONSOLE || type == ORIGIN_TEST) {
			VarNumberSerializer.readSVarLong(from); // ???
		}
		from.readBoolean(); // isInternal
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new Chat(command), Unpooled.wrappedBuffer(readbytes)));
	}

}