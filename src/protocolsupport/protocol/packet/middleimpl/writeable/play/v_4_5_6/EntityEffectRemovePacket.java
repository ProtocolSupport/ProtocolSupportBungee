package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.EntityEffect;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.writeable.LegacySingleWriteablePacket;

public class EntityEffectRemovePacket extends LegacySingleWriteablePacket<EntityEffect> {

	public EntityEffectRemovePacket(int packetId) {
		super(LegacyPacketId.Clientbound.PLAY_ENTITY_EFFECT_REMOVE);
	}

	@Override
	protected void write(ByteBuf data, EntityEffect packet) {
		data.writeInt(packet.getEffectId());
		data.writeByte(packet.getEffectId());
	}

}
