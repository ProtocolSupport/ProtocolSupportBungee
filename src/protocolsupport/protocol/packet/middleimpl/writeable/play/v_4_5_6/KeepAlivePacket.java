package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.KeepAlive;
import protocolsupport.protocol.packet.middleimpl.writeable.SingleWriteablePacket;

public class KeepAlivePacket extends SingleWriteablePacket<KeepAlive> {

	public KeepAlivePacket() {
		super(0x00);
	}

	@Override
	protected void write(ByteBuf data, KeepAlive packet) {
		data.writeInt((int) packet.getRandomId());
	}

}
