package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.ClientStatus;
import protocolsupport.protocol.packet.middle.WriteableMiddlePacket;
import protocolsupport.utils.netty.Allocator;

public class ClientCommandPacket extends WriteableMiddlePacket<ClientStatus> {

	@Override
	public Collection<ByteBuf> toData(ClientStatus packet) {
		if (packet.getPayload() == 1) {
			ByteBuf data = Allocator.allocateBuffer();
			data.writeByte(0xCD);
			data.writeByte(packet.getPayload());
			return Collections.singletonList(data);
		} else {
			return Collections.emptyList();
		}
	}

}
