package protocolsupport.protocol.packet.middleimpl.readable.play.v_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Chat;
import protocolsupport.protocol.packet.middleimpl.readable.DefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class ChatPacket extends DefinedReadableMiddlePacket {

	public static final int PACKET_ID = 0x03;

	public ChatPacket() {
		super(PACKET_ID);
	}

	protected String message;

	@Override
	protected void read0(ByteBuf from) {
		message = LegacySerializer.readString(from);
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new Chat(message), Unpooled.wrappedBuffer(readbytes)));
	}

}
