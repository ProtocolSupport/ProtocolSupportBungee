package protocolsupport.protocol.entitymap;

import io.netty.buffer.ByteBuf;

//TODO: rewrite this as soon as i can actually remember how it works
public class LegacyEntityMap {

	private static boolean[] rewriteBasic = new boolean[256];

	static {
		rewriteBasic[0x05] = true; // EntityEquipment
		rewriteBasic[0x11] = true; // UseBed
		rewriteBasic[0x12] = true; // Animation
		rewriteBasic[0x14] = true; // SpawnNamedEntity
		rewriteBasic[0x16] = true; // CollectItem
		rewriteBasic[0x17] = true; // SpawnObject
		rewriteBasic[0x18] = true; // SpawnMob
		rewriteBasic[0x19] = true; // SpawnPainting
		rewriteBasic[0x1A] = true; // SpawnEXPOrb
		rewriteBasic[0x1C] = true; // EntityVelocity
		rewriteBasic[0x1E] = true; // Entity
		rewriteBasic[0x1F] = true; // EntityRelMove
		rewriteBasic[0x20] = true; // EntityLook
		rewriteBasic[0x21] = true; // EntityRelMoveLook
		rewriteBasic[0x22] = true; // EntityTeleport
		rewriteBasic[0x23] = true; // EntityHeadLook
		rewriteBasic[0x26] = true; // EntityStatus
		rewriteBasic[0x27] = true; // AttachEntity
		rewriteBasic[0x28] = true; // EntityMetadata
		rewriteBasic[0x29] = true; // EntityEffect
		rewriteBasic[0x2A] = true; // RemoveEntityEffect
		rewriteBasic[0x37] = true; // BlockBreakAnimation
		rewriteBasic[0x47] = true; // SpawnGlobalEntity
	}

	public static void rewriteClientbound(ByteBuf buf, int oldId, int newId) {
		int readerIndex = buf.readerIndex();
		int packetId = buf.readByte() & 0xFF;
		if (rewriteBasic[packetId]) {
			rewriteInt(buf, oldId, newId, 1);
		}
		switch (packetId) {
			case 0x16: { // CollectItem
				rewriteInt(buf, oldId, newId, 5);
				break;
			}
			case 0x17: { // SpawnObject
				final int type = buf.readUnsignedByte();
				if ((type == 60) || (type == 90)) {
					buf.skipBytes(14);
					int position = buf.readerIndex();
					int readId = buf.readInt();
					int changedId = -1;
					if (readId == oldId) {
						buf.setInt(position, newId);
						changedId = newId;
					} else if (readId == newId) {
						buf.setInt(position, oldId);
						changedId = oldId;
					}
					if (changedId != -1) {
						if ((changedId == 0) && (readId != 0)) {
							buf.readerIndex(readerIndex);
							buf.writerIndex(buf.readableBytes() - 6);
						} else if ((changedId != 0) && (readId == 0)) {
							buf.readerIndex(readerIndex);
							buf.capacity(buf.readableBytes() + 6);
							buf.writerIndex(buf.readableBytes() + 6);
						}
					}
				}
				break;
			}
			case 0x1D: { // DestroyEntities
				final int count = buf.readByte();
				final int[] ids = new int[count];
				for (int i = 0; i < count; ++i) {
					ids[i] = buf.readInt();
				}
				buf.readerIndex(readerIndex + 1);
				buf.writerIndex(readerIndex + 1);
				buf.writeByte(count);
				for (int id : ids) {
					if (id == oldId) {
						id = newId;
					} else if (id == newId) {
						id = oldId;
					}
					buf.writeInt(id);
				}
				break;
			}
			case 0x27: { // AttachEntity
				rewriteInt(buf, oldId, newId, 5);
				break;
			}
		}
		buf.readerIndex(readerIndex);
	}

	public static void rewriteServerbound(ByteBuf buf, int oldId, int newId) {
		int readerIndex = buf.readerIndex();
		int packetId = buf.readByte() & 0xFF;
		switch (packetId) {
			case 0x07: { // UseEntity
				rewriteInt(buf, oldId, newId, 1);
				break;
			}
			case 0x13: { // EntityAction
				rewriteInt(buf, oldId, newId, 1);
				break;
			}
		}
		buf.readerIndex(readerIndex);
	}

	protected static void rewriteInt(final ByteBuf packet, final int oldId, final int newId, final int offset) {
		final int readId = packet.getInt(offset);
		if (readId == oldId) {
			packet.setInt(offset, newId);
		} else if (readId == newId) {
			packet.setInt(offset, oldId);
		}
	}

}
