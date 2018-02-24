package protocolsupport.protocol.packet.middleimpl.readable.play.v_pe;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.Validate;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Chat;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.PEDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class FromClientChatPacket extends PEDefinedReadableMiddlePacket {

	public static final int CLIENT_CHAT_TYPE = 1;

	protected String message;

	public FromClientChatPacket() {
		super(PEPacketId.Dualbound.PLAY_CHAT);
	}

	@Override
	protected void read0(ByteBuf from) {
		int type = from.readUnsignedByte();
		Validate.isTrue(type == CLIENT_CHAT_TYPE, MessageFormat.format("Unexcepted serverbound chat type, expected {0}, but received {1}", CLIENT_CHAT_TYPE, type));
		from.readBoolean(); //needs translation
		StringSerializer.readVarIntUTF8String(from); //skip sender
		message = StringSerializer.readVarIntUTF8String(from);
		StringSerializer.readVarIntUTF8String(from); //Xbox user ID
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new Chat(message), Unpooled.wrappedBuffer(readbytes)));
	}

}
