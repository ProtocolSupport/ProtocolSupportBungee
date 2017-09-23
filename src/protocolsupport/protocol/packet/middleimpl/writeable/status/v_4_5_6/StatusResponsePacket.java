package protocolsupport.protocol.packet.middleimpl.writeable.status.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.StatusResponse;
import protocolsupport.protocol.packet.middle.WriteableMiddlePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6.KickPacket;
import protocolsupport.utils.PingSerializer;

public class StatusResponsePacket extends WriteableMiddlePacket<StatusResponse> {

	@Override
	public Collection<ByteBuf> toData(StatusResponse packet) {
		return Collections.singletonList(KickPacket.create(PingSerializer.fromJSON(connection.getVersion().getId(), packet.getResponse())));
	}

}
