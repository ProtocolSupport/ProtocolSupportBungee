package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.TabCompleteResponse;
import protocolsupport.protocol.packet.middleimpl.writeable.SingleWriteablePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class TabCompleteResponsePacket extends SingleWriteablePacket<TabCompleteResponse> {

	public TabCompleteResponsePacket() {
		super(0xCB);
	}

	@Override
	protected void write(ByteBuf data, TabCompleteResponse packet) {
		LegacySerializer.writeString(data, String.join("\u0000", packet.getCommands()));
	}

}
