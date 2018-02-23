package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.ClientSettings;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class ClientSettingsPacket extends LegacyDefinedReadableMiddlePacket {

	public ClientSettingsPacket() {
		super(LegacyPacketId.Serverbound.PLAY_CLIENT_SETTINGS);
	}

	protected String locale;
	protected byte viewDistance;
	protected int chatFlags;
	protected boolean chatColours;
	protected byte difficulty;

	@Override
	protected void read0(ByteBuf from) {
		locale = StringSerializer.readShortUTF16BEString(from);
		viewDistance = from.readByte();
		byte chatState = from.readByte();
		chatFlags = (byte) (chatState & 7);
		chatColours = ((chatState & 8) == 8);
		difficulty = from.readByte();
		from.readBoolean();
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new ClientSettings(locale, viewDistance, chatFlags, chatColours, difficulty, (byte) 255, 1), Unpooled.wrappedBuffer(readbytes)));
	}

}
