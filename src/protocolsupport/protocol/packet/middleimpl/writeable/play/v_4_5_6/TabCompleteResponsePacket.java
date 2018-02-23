package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.TabCompleteResponse;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.writeable.LegacySingleWriteablePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class TabCompleteResponsePacket extends LegacySingleWriteablePacket<TabCompleteResponse> {

	public TabCompleteResponsePacket() {
		super(LegacyPacketId.Dualbound.PLAY_TAB_COMPLETE);
	}

	@Override
	protected void write(ByteBuf data, TabCompleteResponse packet) {
		StringSerializer.writeShortUTF16BEString(data, String.join("\u0000", packet.getCommands()));
	}

}
