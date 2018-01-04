package protocolsupport.protocol.entitymap;

import java.util.BitSet;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.packet.id.LegacyPacketId;

public class LegacyEntityMap extends EntityMap {

	private static BitSet packetsWithEntityId = new BitSet(256);

	static {
		packetsWithEntityId.set(LegacyPacketId.ENTITY_EQUIPMENT);
		packetsWithEntityId.set(LegacyPacketId.USE_BED);
		packetsWithEntityId.set(LegacyPacketId.ANIMATION);
		packetsWithEntityId.set(LegacyPacketId.SPAWN_NAMED_ENTITY);
		packetsWithEntityId.set(LegacyPacketId.COLLECT_ENTITY);
		packetsWithEntityId.set(LegacyPacketId.SPAWN_OBJECT);
		packetsWithEntityId.set(LegacyPacketId.SPAWN_MOB);
		packetsWithEntityId.set(LegacyPacketId.SPAWN_PAINTING);
		packetsWithEntityId.set(LegacyPacketId.SPAWN_EXP_ORB);
		packetsWithEntityId.set(LegacyPacketId.ENTITY_VELOCITY);
		packetsWithEntityId.set(LegacyPacketId.ENTITY);
		packetsWithEntityId.set(LegacyPacketId.ENTITY_REL_MOVE);
		packetsWithEntityId.set(LegacyPacketId.ENTITY_LOOK);
		packetsWithEntityId.set(LegacyPacketId.ENTITY_REL_MOVE_LOOK);
		packetsWithEntityId.set(LegacyPacketId.ENTITY_TELEPORT);
		packetsWithEntityId.set(LegacyPacketId.ENTITY_HEAD_ROTATION);
		packetsWithEntityId.set(LegacyPacketId.ENTITY_STATUS);
		packetsWithEntityId.set(LegacyPacketId.ENTITY_ATTACH);
		packetsWithEntityId.set(LegacyPacketId.ENTITY_METADATA);
		packetsWithEntityId.set(LegacyPacketId.ENTITY_EFFECT_ADD);
		packetsWithEntityId.set(LegacyPacketId.ENTITY_EFFECT_REMOVE);
		packetsWithEntityId.set(LegacyPacketId.BLOCK_BREAK_ANIMATION);
		packetsWithEntityId.set(LegacyPacketId.SPAWN_GLOBALENTITY);
		packetsWithEntityId.set(LegacyPacketId.USE_ENTITY);
		packetsWithEntityId.set(LegacyPacketId.ENTITY_ACTION);
	}

	@Override
	public void rewriteClientbound(ByteBuf buf, int oldId, int newId) {
		buf.markReaderIndex();
		int packetId = buf.readUnsignedByte();
		if (packetsWithEntityId.get(packetId)) {
			rewriteInt(buf, oldId, newId, Byte.BYTES);
		}
		switch (packetId) {
			case LegacyPacketId.COLLECT_ENTITY:
			case LegacyPacketId.ENTITY_ATTACH: {
				rewriteInt(buf, oldId, newId, Byte.BYTES + Integer.BYTES);
				break;
			}
			case LegacyPacketId.ENTITY_DESTROY: {
				final int count = buf.readByte();
				for (int i = 0; i < count; ++i) {
					rewriteInt(buf, oldId, newId, Byte.BYTES * 2 + count * Integer.BYTES);
				}
				break;
			}
			case LegacyPacketId.SPAWN_OBJECT: {
				final int type = buf.readUnsignedByte();
				if (
					(type == 60) || //arrow
					(type == 90) || //finishing hook
					(type == 63) || //fire ball
					(type == 64) || //fire charge
					(type == 66)    //wither skull
				) {
					buf.skipBytes(Integer.BYTES * 3 + Byte.BYTES * 2);
					int oldObjData = buf.readInt();
					int newObjData = rewriteInt(buf, oldId, newId, buf.readerIndex() - Integer.BYTES);
					if (newObjData != -1) {
						if ((newObjData == 0) && (oldObjData != 0)) {
							buf.writerIndex(buf.readerIndex());
						} else if ((newObjData != 0) && (oldObjData == 0)) {
							buf.capacity(buf.readerIndex() + Short.BYTES * 3);
							buf.writerIndex(buf.readerIndex() + Short.BYTES * 3);
						}
					}
				}
				break;
			}
		}
		buf.resetReaderIndex();
	}

	@Override
	public void rewriteServerbound(ByteBuf buf, int oldId, int newId) {
		buf.markReaderIndex();
		int packetId = buf.readUnsignedByte();
		if (packetsWithEntityId.get(packetId)) {
			rewriteInt(buf, oldId, newId, Byte.BYTES);
		}
		buf.resetReaderIndex();
	}

}
