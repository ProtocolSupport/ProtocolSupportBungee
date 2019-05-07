package protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.PluginMessage;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middleimpl.writeable.PESingleWriteablePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class CustomEventPacket extends PESingleWriteablePacket<PluginMessage> {

	public CustomEventPacket() {
		super(PEPacketId.Dualbound.CUSTOM_EVENT);
	}

	@Override
	protected void write(ByteBuf data, PluginMessage packet) {
		write(data, packet.getTag(), packet.getData());
	}

	static void write(ByteBuf buf, String tag, byte[] data) {
		StringSerializer.writeVarIntUTF8String(buf, tag);
		buf.writeBytes(data);
	}

}
