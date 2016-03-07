package protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.transformer.TransformedPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.PacketDataSerializer;
import protocolsupport.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.protocol.packet.Kick;

public class KickPacket extends Kick implements TransformedPacket {

	private String message;

	public KickPacket() {
	}

	public KickPacket(String message) {
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
		return ComponentSerializer.toString(new TextComponent(message));
	}

	@Override
	public void setMessage(final String message) {
		this.message = Utils.toLegacyText(message);
	}

	@Override
	public boolean shouldWrite() {
		return true;
	}

	@Override
	public int getId() {
		return 0xFF;
	}

}
