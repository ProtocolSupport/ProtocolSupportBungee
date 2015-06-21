package protocolsupport.protocol.transformer;

import io.netty.buffer.ByteBuf;

public interface TransformedPacket {

	public void write(ByteBuf buf);

	public boolean shouldWrite();

	public int getId();

}
