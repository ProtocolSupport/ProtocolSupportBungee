package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDynamicLengthPassthroughReadableMiddlePacket;
import protocolsupport.protocol.serializer.TypeCopier;

public class InventoryCreativeSetSlotPacket extends LegacyDynamicLengthPassthroughReadableMiddlePacket {

	public InventoryCreativeSetSlotPacket() {
		super(LegacyPacketId.Serverbound.PLAY_INVENTORY_CREATIVE_SET_SLOT);
	}

	@Override
	protected void readTo(ByteBuf data, ByteBuf to) {
		TypeCopier.copyBytes(data, to, Short.BYTES);
		TypeCopier.copyLegacyItemStack(data, to);
	}

}
