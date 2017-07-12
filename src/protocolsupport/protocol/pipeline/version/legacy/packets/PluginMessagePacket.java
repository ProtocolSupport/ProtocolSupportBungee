package protocolsupport.protocol.pipeline.version.legacy.packets;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.PluginMessage;
import protocolsupport.protocol.serializer.LegacySerializer;

public class PluginMessagePacket extends PluginMessage implements TransformedPacket {

	private String tag;
	private byte[] data;
	private boolean allowExtendedPacket;

	public PluginMessagePacket() {
	}

	public PluginMessagePacket(String string, byte[] clone, boolean serverForge) {
		this.tag = string;
		this.data = clone;
		this.allowExtendedPacket = serverForge;
	}

	@Override
	public void read(ByteBuf buf) {
		tag = LegacySerializer.readString(buf);
		data = LegacySerializer.readArray(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		LegacySerializer.writeString(tag, buf);
		LegacySerializer.writeArray(data, buf);
	}

	@Override
	public DataInput getStream() {
		return new DataInputStream(new ByteArrayInputStream(this.data));
	}

	@Override
	public String getTag() {
		return this.tag;
	}

	@Override
	public byte[] getData() {
		return this.data;
	}

	@Override
	public boolean isAllowExtendedPacket() {
		return this.allowExtendedPacket;
	}

	@Override
	public void setTag(final String tag) {
		this.tag = tag;
	}

	@Override
	public void setData(final byte[] data) {
		this.data = data;
	}

	@Override
	public void setAllowExtendedPacket(final boolean allowExtendedPacket) {
		this.allowExtendedPacket = allowExtendedPacket;
	}

	@Override
	public int getId() {
		return 0xFA;
	}

}
