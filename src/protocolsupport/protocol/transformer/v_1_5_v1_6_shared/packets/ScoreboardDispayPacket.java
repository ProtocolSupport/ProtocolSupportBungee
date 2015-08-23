package protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.transformer.TransformedPacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.PacketDataSerializer;
import net.md_5.bungee.protocol.packet.ScoreboardDisplay;

public class ScoreboardDispayPacket extends ScoreboardDisplay implements TransformedPacket {

	private byte position;
	private String name;

	public ScoreboardDispayPacket() {
	}

	public ScoreboardDispayPacket(byte position, String name) {
		this.position = position;
		this.name = name;
	}

	@Override
	public void read(ByteBuf buf) {
		position = buf.readByte();
		name = PacketDataSerializer.readString(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeByte(position);
		PacketDataSerializer.writeString(name, buf);
	}

	@Override
	public byte getPosition() {
		return this.position;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setPosition(final byte position) {
		this.position = position;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public boolean shouldWrite() {
		return true;
	}

	@Override
	public int getId() {
		return 0xD0;
	}

}
