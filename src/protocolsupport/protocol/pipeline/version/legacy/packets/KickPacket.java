package protocolsupport.protocol.pipeline.version.legacy.packets;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.protocol.packet.Kick;
import protocolsupport.protocol.serializer.LegacySerializer;
import protocolsupport.utils.Utils;

public class KickPacket extends Kick implements TransformedPacket {

	private String message;

	public KickPacket() {
	}

	public KickPacket(String message) {
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
		return ComponentSerializer.toString(new TextComponent(message));
	}

	@Override
	public void setMessage(final String message) {
		this.message = Utils.toLegacyText(message);
	}

	@Override
	public int getId() {
		return 0xFF;
	}

}
