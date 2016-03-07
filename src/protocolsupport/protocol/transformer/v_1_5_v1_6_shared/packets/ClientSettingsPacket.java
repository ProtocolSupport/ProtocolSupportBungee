package protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.ClientSettings;
import protocolsupport.protocol.transformer.TransformedPacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.PacketDataSerializer;

public class ClientSettingsPacket extends ClientSettings implements TransformedPacket {

	private String locale;
	private byte viewDistance;
	private int chatFlags;
	private boolean chatColours;
	private byte difficulty;

	@Override
	public void read(ByteBuf buf) {
		locale = PacketDataSerializer.readString(buf);
		viewDistance = buf.readByte();
		byte chatState = buf.readByte();
		chatFlags = (byte) (chatState & 7);
		chatColours = ((chatState & 8) == 8);
		difficulty = buf.readByte();
		buf.readBoolean();
	}

	@Override
	public void write(ByteBuf buf) {
		PacketDataSerializer.writeString(locale, buf);
		buf.writeByte(viewDistance);
		buf.writeByte(chatColours ? ((1 << 4) | chatFlags) : chatFlags);
		buf.writeByte(difficulty);
		buf.writeBoolean(true);
	}

	@Override
	public String getLocale() {
		return this.locale;
	}

	@Override
	public byte getViewDistance() {
		return this.viewDistance;
	}

	@Override
	public int getChatFlags() {
		return this.chatFlags;
	}

	@Override
	public boolean isChatColours() {
		return this.chatColours;
	}

	@Override
	public byte getDifficulty() {
		return this.difficulty;
	}

	@Override
	public void setLocale(final String locale) {
		this.locale = locale;
	}

	@Override
	public void setViewDistance(final byte viewDistance) {
		this.viewDistance = viewDistance;
	}

	@Override
	public void setChatFlags(final int chatFlags) {
		this.chatFlags = chatFlags;
	}

	@Override
	public void setChatColours(final boolean chatColours) {
		this.chatColours = chatColours;
	}

	@Override
	public void setDifficulty(final byte difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public boolean shouldWrite() {
		return true;
	}

	@Override
	public int getId() {
		return 0xCC;
	}

}
