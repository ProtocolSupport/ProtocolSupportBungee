package protocolsupport.protocol.packet.middleimpl.readable.play.v_pe;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Kick;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.PEDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class KickPacket extends PEDefinedReadableMiddlePacket {

	public KickPacket() {
		super(PEPacketId.Clientbound.PLAY_KICK);
	}

	protected String message;

	@Override
	protected void read0(ByteBuf from) {
		from.readBoolean(); //hide disconnection screen
		message = ComponentSerializer.toString(new TextComponent(StringSerializer.readVarIntUTF8String(from)));
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new Kick(message), Unpooled.wrappedBuffer(readbytes)));
	}

}
