package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Kick;
import protocolsupport.protocol.packet.middleimpl.readable.DefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class KickPacket extends DefinedReadableMiddlePacket {

	public static final int PACKET_ID = 0xFF;

	public KickPacket() {
		super(PACKET_ID);
	}

	protected String message;

	@Override
	protected void read0(ByteBuf from) {
		message = ComponentSerializer.toString(new TextComponent(LegacySerializer.readString(from)));
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new Kick(message), Unpooled.wrappedBuffer(readbytes)));
	}

}
