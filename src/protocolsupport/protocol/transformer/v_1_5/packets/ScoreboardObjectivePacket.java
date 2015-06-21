package protocolsupport.protocol.transformer.v_1_5.packets;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.transformer.TransformedPacket;
import protocolsupport.protocol.transformer.v_1_5.PacketDataSerializer;
import net.md_5.bungee.protocol.packet.ScoreboardObjective;

public class ScoreboardObjectivePacket extends ScoreboardObjective implements TransformedPacket {

	private String name;
	private String value;
	private String type = "integer";
	private byte action;

	public ScoreboardObjectivePacket() {
	}

	public ScoreboardObjectivePacket(String name, String value, byte action) {
		this.name = name;
		this.value = value;
		this.action = action;
	}

	@Override
	public void read(ByteBuf buf) {
		name = PacketDataSerializer.readString(buf);
		value = PacketDataSerializer.readString(buf);
		action = buf.readByte();
	}

	@Override
	public void write(ByteBuf buf) {
		PacketDataSerializer.writeString(name, buf);
		PacketDataSerializer.writeString(value, buf);
		buf.writeByte(action);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public byte getAction() {
		return this.action;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public void setValue(final String value) {
		this.value = value;
	}

	@Override
	public void setType(final String type) {
		this.type = type;
	}

	@Override
	public void setAction(final byte action) {
		this.action = action;
	}

	@Override
	public boolean shouldWrite() {
		return true;
	}

	@Override
	public int getId() {
		return 0xCE;
	}

}
