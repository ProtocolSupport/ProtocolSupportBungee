package protocolsupport.protocol.pipeline.version.legacy.packets;

import io.netty.buffer.ByteBuf;

public interface TransformedPacket {

	public void write(ByteBuf buf);

	public boolean shouldWrite();

	public int getId();

	@Override
	public String toString();

}
