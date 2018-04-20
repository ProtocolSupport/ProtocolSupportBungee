package protocolsupport.protocol.packet.middleimpl.readable.play.v_pe;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Chat;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.PEDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class FromServerChatPacket extends PEDefinedReadableMiddlePacket {

	public FromServerChatPacket() {
		super(PEPacketId.Dualbound.PLAY_CHAT);
	}

	protected byte type;
	protected String message;

	@Override
	protected void read0(ByteBuf from) {
		type = (byte) (from.readUnsignedByte() == 5 ? 2 : 0);
		from.readBoolean(); //needs translation
		message = ComponentSerializer.toString(new TextComponent(StringSerializer.readVarIntUTF8String(from)));
		StringSerializer.readVarIntUTF8String(from); //Xbox user ID
		StringSerializer.readVarIntUTF8String(from); //Platform Chat ID
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new Chat(message, type), Unpooled.wrappedBuffer(readbytes)));
	}

}
