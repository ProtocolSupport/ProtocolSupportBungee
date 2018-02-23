package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Chat;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class FromClientChatPacket extends LegacyDefinedReadableMiddlePacket {

	public FromClientChatPacket() {
		super(LegacyPacketId.Dualbound.PLAY_CHAT);
	}

	protected String message;

	@Override
	protected void read0(ByteBuf from) {
		message = StringSerializer.readShortUTF16BEString(from);
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new Chat(message), Unpooled.wrappedBuffer(readbytes)));
	}

}
