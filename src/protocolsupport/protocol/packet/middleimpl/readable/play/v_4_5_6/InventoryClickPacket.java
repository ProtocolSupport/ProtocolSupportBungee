package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.packet.middleimpl.readable.DynamicLengthPassthroughReadableMiddlePacket;
import protocolsupport.protocol.serializer.TypeCopier;

public class InventoryClickPacket extends DynamicLengthPassthroughReadableMiddlePacket {

	public static final int PACKET_ID = 0x66;

	public InventoryClickPacket() {
		super(PACKET_ID);
	}

	@Override
	protected void readTo(ByteBuf data, ByteBuf to) {
		TypeCopier.copyBytes(data, to, Byte.BYTES + Short.BYTES + Byte.BYTES + Short.BYTES + Byte.BYTES);
		TypeCopier.copyItemStack(data, to);
	}

}
