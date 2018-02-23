package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.ClientStatus;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDefinedReadableMiddlePacket;

public class ClientCommandPacket extends LegacyDefinedReadableMiddlePacket {

	public ClientCommandPacket() {
		super(LegacyPacketId.Serverbound.LOGIN_PLAY_CLIENT_COMMAND);
	}

	protected int status;

	@Override
	protected void read0(ByteBuf from) {
		status = from.readByte();
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		if (status == 1) {
			return Collections.singletonList(new PacketWrapper(new ClientStatus((byte) status), Unpooled.wrappedBuffer(readbytes)));
		} else {
			return Collections.emptyList();
		}
	}

}
