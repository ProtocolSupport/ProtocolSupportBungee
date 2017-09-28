package protocolsupport.protocol.packet.middleimpl.readable.login.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketWrapper;
import protocolsupport.protocol.packet.middle.ReadableMiddlePacket;

public class LoginClientCommandPacket extends ReadableMiddlePacket {

	public static final int PACKET_ID = 0xCD;

	@Override
	public void read(ByteBuf data) {
		data.readByte();
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.emptyList();
	}

}
