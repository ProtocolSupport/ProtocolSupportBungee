package protocolsupport.protocol.packet.middleimpl.readable.handshake.v_pe;

import java.util.Arrays;
import java.util.Collection;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Handshake;
import net.md_5.bungee.protocol.packet.StatusRequest;
import protocolsupport.injector.pe.PEProxyServerInfoHandler;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDefinedReadableMiddlePacket;
import protocolsupport.protocol.utils.ProtocolVersionsHelper;

public class PingHandshakePacket extends LegacyDefinedReadableMiddlePacket {

	public PingHandshakePacket() {
		super(PEProxyServerInfoHandler.PACKET_ID);
	}

	@Override
	protected void read0(ByteBuf from) {
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Arrays.asList(
				new PacketWrapper(new Handshake(ProtocolVersionsHelper.LATEST_PC.getId(), "127.0.0.1", 25565, 1), Unpooled.EMPTY_BUFFER),
				new PacketWrapper(new StatusRequest(), Unpooled.wrappedBuffer(readbytes))
		);
	}

}
