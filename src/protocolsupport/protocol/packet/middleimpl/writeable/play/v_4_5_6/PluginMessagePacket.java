package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.PluginMessage;
import protocolsupport.protocol.packet.middleimpl.writeable.SingleWriteablePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class PluginMessagePacket extends SingleWriteablePacket<PluginMessage> {

	public PluginMessagePacket() {
		super(0xFA);
	}

	@Override
	protected void write(ByteBuf data, PluginMessage packet) {
		LegacySerializer.writeString(data, packet.getTag());
		LegacySerializer.writeArray(data, packet.getData());
	}

}
