package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.PluginMessage;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.writeable.LegacySingleWriteablePacket;
import protocolsupport.protocol.serializer.ArraySerializer;
import protocolsupport.protocol.serializer.StringSerializer;

public class PluginMessagePacket extends LegacySingleWriteablePacket<PluginMessage> {

	public PluginMessagePacket() {
		super(LegacyPacketId.Dualbound.PLAY_PLUGIN_MESSAGE);
	}

	@Override
	protected void write(ByteBuf data, PluginMessage packet) {
		StringSerializer.writeShortUTF16BEString(data, packet.getTag());
		ArraySerializer.writeShortLengthByteArray(data, packet.getData());
	}

}
