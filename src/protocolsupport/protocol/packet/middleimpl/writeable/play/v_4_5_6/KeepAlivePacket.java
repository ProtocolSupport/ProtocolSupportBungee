package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.KeepAlive;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.writeable.LegacySingleWriteablePacket;

public class KeepAlivePacket extends LegacySingleWriteablePacket<KeepAlive> {

	public KeepAlivePacket() {
		super(LegacyPacketId.Dualbound.PLAY_KEEP_ALIVE);
	}

	@Override
	protected void write(ByteBuf data, KeepAlive packet) {
		data.writeInt((int) packet.getRandomId());
	}

}
