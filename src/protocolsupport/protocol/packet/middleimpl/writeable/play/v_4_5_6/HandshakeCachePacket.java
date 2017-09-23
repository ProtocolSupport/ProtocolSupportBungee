package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Handshake;
import protocolsupport.protocol.packet.middle.WriteableMiddlePacket;

public class HandshakeCachePacket extends WriteableMiddlePacket<Handshake> {

	@Override
	public Collection<ByteBuf> toData(Handshake packet) {
		cache.serverHandshake = packet;
		return Collections.emptyList();
	}

}
