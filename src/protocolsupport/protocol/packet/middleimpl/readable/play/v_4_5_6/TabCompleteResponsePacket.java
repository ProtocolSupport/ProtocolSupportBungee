package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.TabCompleteResponse;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class TabCompleteResponsePacket extends LegacyDefinedReadableMiddlePacket {

	public TabCompleteResponsePacket() {
		super(LegacyPacketId.Dualbound.PLAY_TAB_COMPLETE);
	}

	protected String string;

	@Override
	protected void read0(ByteBuf from) {
		string = StringSerializer.readShortUTF16BEString(from);
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new TabCompleteResponse(Arrays.asList(string.split("\u0000"))), Unpooled.wrappedBuffer(readbytes)));
	}

}
