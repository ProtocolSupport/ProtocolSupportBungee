package protocolsupport.protocol.packet.middleimpl.writeable.status.v_pe;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.StatusResponse;
import protocolsupport.injector.pe.PEProxyServerInfoHandler;
import protocolsupport.protocol.packet.middleimpl.writeable.PESingleWriteablePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class StatusResponsePacket extends PESingleWriteablePacket<StatusResponse> {

	public StatusResponsePacket() {
		super(PEProxyServerInfoHandler.PACKET_ID);
	}

	@Override
	protected void write(ByteBuf data, StatusResponse packet) {
		StringSerializer.writeVarIntUTF8String(data, packet.getResponse());
	}

}
