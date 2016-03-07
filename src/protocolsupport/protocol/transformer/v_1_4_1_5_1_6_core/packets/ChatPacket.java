package protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.transformer.TransformedPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.PacketDataSerializer;
import net.md_5.bungee.protocol.packet.Chat;

public class ChatPacket extends Chat implements TransformedPacket {

	private String message;

	public ChatPacket() {
	}

	public ChatPacket(String message) {
		this.message = message;
	}

	@Override
	public void read(ByteBuf buf) {
		message = PacketDataSerializer.readString(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		PacketDataSerializer.writeString(message, buf);
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public byte getPosition() {
		return 0;
	}

	@Override
	public void setPosition(byte position) {
	}

	@Override
	public int getId() {
		return 0x03;
	}

	@Override
	public boolean shouldWrite() {
		return true;
	}

}
