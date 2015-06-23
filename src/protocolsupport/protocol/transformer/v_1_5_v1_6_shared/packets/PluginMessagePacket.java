package protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;

import protocolsupport.protocol.transformer.TransformedPacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.PacketDataSerializer;
import net.md_5.bungee.protocol.MinecraftInput;
import net.md_5.bungee.protocol.packet.PluginMessage;

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
		tag = PacketDataSerializer.readString(buf);
		data = PacketDataSerializer.readArray(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		PacketDataSerializer.writeString(tag, buf);
		PacketDataSerializer.writeArray(data, buf);
	}

	@Override
	public DataInput getStream() {
		return new DataInputStream(new ByteArrayInputStream(this.data));
	}

	@Override
	public MinecraftInput getMCStream() {
		return new MinecraftInput(Unpooled.wrappedBuffer(this.data));
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
	public boolean shouldWrite() {
		return true;
	}

	@Override
	public int getId() {
		return 0xFA;
	}

}
