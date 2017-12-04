package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.PluginMessage;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.ArraySerializer;
import protocolsupport.protocol.serializer.StringSerializer;

public class PluginMessagePacket extends LegacyDefinedReadableMiddlePacket {

	public static final int PACKET_ID = 0xFA;

	public PluginMessagePacket() {
		super(PACKET_ID);
	}

	protected String tag;
	protected byte[] data;

	@Override
	protected void read0(ByteBuf from) {
		tag = StringSerializer.readShortUTF16BEString(from);
		data = ArraySerializer.readShortLengthByteArray(from);
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new PluginMessage(tag, data, true), Unpooled.wrappedBuffer(readbytes)));
	}

}
