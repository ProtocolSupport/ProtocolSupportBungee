package protocolsupport.protocol.pipeline.version.legacy.packets;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.StatusRequest;

public class StatusRequestPacket extends StatusRequest implements TransformedPacket {

	@Override
	public void read(ByteBuf buf) {
		buf.readByte();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeByte(1);
	}

	@Override
	public int getId() {
		return 0xFE;
	}

}
