package protocolsupport.protocol.pipeline.version.legacy.packets;

import io.netty.buffer.ByteBuf;

public interface TransformedPacket {

	public void read(ByteBuf buf);

	public void write(ByteBuf buf);

	public int getId();

}
