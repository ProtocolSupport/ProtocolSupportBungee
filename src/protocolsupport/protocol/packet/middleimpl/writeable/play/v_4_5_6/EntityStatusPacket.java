package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.EntityStatus;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.writeable.LegacySingleWriteablePacket;

public class EntityStatusPacket extends LegacySingleWriteablePacket<EntityStatus> {

	public EntityStatusPacket() {
		super(LegacyPacketId.Clientbound.PLAY_ENTITY_STATUS);
	}

	@Override
	protected void write(ByteBuf data, EntityStatus packet) {
		data.writeInt(packet.getEntityId());
		data.writeByte(packet.getStatus());
	}

}
