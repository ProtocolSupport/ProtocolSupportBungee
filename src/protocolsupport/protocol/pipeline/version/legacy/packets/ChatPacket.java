package protocolsupport.protocol.pipeline.version.legacy.packets;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Chat;
import protocolsupport.protocol.serializer.LegacySerializer;

public class ChatPacket extends Chat implements TransformedPacket {

	private String message;

	public ChatPacket() {
	}

	public ChatPacket(String message) {
		this.message = message;
	}

	@Override
	public void read(ByteBuf buf) {
		message = LegacySerializer.readString(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		LegacySerializer.writeString(message, buf);
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
