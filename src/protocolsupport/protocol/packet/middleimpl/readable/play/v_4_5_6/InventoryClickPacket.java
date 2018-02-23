package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDynamicLengthPassthroughReadableMiddlePacket;
import protocolsupport.protocol.serializer.TypeCopier;

public class InventoryClickPacket extends LegacyDynamicLengthPassthroughReadableMiddlePacket {

	public InventoryClickPacket() {
		super(LegacyPacketId.Serverbound.PLAY_INVENTORY_CLICK);
	}

	@Override
	protected void readTo(ByteBuf data, ByteBuf to) {
		TypeCopier.copyBytes(data, to, Byte.BYTES + Short.BYTES + Byte.BYTES + Short.BYTES + Byte.BYTES);
		TypeCopier.copyLegacyItemStack(data, to);
	}

}
