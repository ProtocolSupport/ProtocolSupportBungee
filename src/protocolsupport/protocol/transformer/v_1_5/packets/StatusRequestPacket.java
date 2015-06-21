package protocolsupport.protocol.transformer.v_1_5.packets;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.transformer.TransformedPacket;
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
	public boolean shouldWrite() {
		return true;
	}

	@Override
	public int getId() {
		return 0xFE;
	}

}
