package protocolsupport.protocol.packet.middleimpl.writeable.status.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.StatusResponse;
import protocolsupport.protocol.packet.middle.WriteableMiddlePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6.KickPacket;
import protocolsupport.utils.PingSerializer;
import protocolsupport.utils.PingSerializer.ServerPing;

public class StatusResponsePacket extends WriteableMiddlePacket<StatusResponse> {

	@Override
	public Collection<ByteBuf> toData(StatusResponse packet) {
		return Collections.singletonList(KickPacket.create(convert(connection.getVersion().getId(), PingSerializer.fromJson(packet.getResponse()))));
	}

	protected static String convert(int protocolVersion, ServerPing serverPing) {
		return
		"§1\u0000" +
		protocolVersion +
		"\u0000" +
		serverPing.getVersion().getName() +
		"\u0000" +
		serverPing.getMotd().toLegacyText() +
		"\u0000" +
		serverPing.getPlayers().getOnlineCount() +
		"\u0000" +
		serverPing.getPlayers().getMaxPlayers();
	}

}
