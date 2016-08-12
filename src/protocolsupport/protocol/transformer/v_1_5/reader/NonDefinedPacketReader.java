package protocolsupport.protocol.transformer.v_1_5.reader;

import java.util.Arrays;

import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.PacketDataSerializer;
import protocolsupport.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NonDefinedPacketReader {

	private static final int NOT_SUPPORTED = -2;
	private static final int MUST_READ_ALL = -1;

	private static final int[] PACKET_LENGTH = new int[256];

	static {
		Arrays.fill(PACKET_LENGTH, NOT_SUPPORTED);
		PACKET_LENGTH[0x04] = 16; //TimeUpdate
		PACKET_LENGTH[0x05] = MUST_READ_ALL; //EntityEquipment
		PACKET_LENGTH[0x06] = 12; //SpawnPosition
		PACKET_LENGTH[0x07] = 9; //UseEntity 
		PACKET_LENGTH[0x08] = 8; //UpdateHealth
		PACKET_LENGTH[0x0A] = 1; //Player
		PACKET_LENGTH[0x0B] = 33; //PlayerPosition
		PACKET_LENGTH[0x0C] = 9; //PlayerLook
		PACKET_LENGTH[0x0D] = 41; //PlayerPositionAndLook
		PACKET_LENGTH[0x0E] = 11; //PlayerDigging
		PACKET_LENGTH[0x0F] = MUST_READ_ALL; //PlayerBlockPlace
		PACKET_LENGTH[0x10] = 2; //HeldItemChange
		PACKET_LENGTH[0x11] = 14; //UseBed
		PACKET_LENGTH[0x12] = 5; //Animation
		PACKET_LENGTH[0x13] = 5; //EntityAction
		PACKET_LENGTH[0x14] = MUST_READ_ALL; //SpawnNamedEntity
		PACKET_LENGTH[0x16] = 8; //CollectItem
		PACKET_LENGTH[0x17] = MUST_READ_ALL; //SpawnObject
		PACKET_LENGTH[0x18] = MUST_READ_ALL; //SpawnMob
		PACKET_LENGTH[0x19] = MUST_READ_ALL; //SpawnPainting
		PACKET_LENGTH[0x1A] = 18; //SpawnEXPOrb
		PACKET_LENGTH[0x1C] = 10; //EntityVelocity
		PACKET_LENGTH[0x1D] = MUST_READ_ALL; //DestroyEntity
		PACKET_LENGTH[0x1E] = 4; //Entity
		PACKET_LENGTH[0x1F] = 7; //EntityRelMove
		PACKET_LENGTH[0x20] = 6; //EntityLook
		PACKET_LENGTH[0x21] = 9; //EntityLookRelMove
		PACKET_LENGTH[0x22] = 18; //EntityTeleport
		PACKET_LENGTH[0x23] = 5; //EntityHeadLook
		PACKET_LENGTH[0x26] = 5; //EntityStatus
		PACKET_LENGTH[0x27] = 8; //AttachEntity
		PACKET_LENGTH[0x28] = MUST_READ_ALL; //EntityMetadata
		PACKET_LENGTH[0x29] = 8; //EntityEffect
		PACKET_LENGTH[0x2A] = 5; //RemoveEntityEffect
		PACKET_LENGTH[0x2B] = 8; //SetEXP
		PACKET_LENGTH[0x33] = MUST_READ_ALL; //ChunkData
		PACKET_LENGTH[0x34] = MUST_READ_ALL; //MultiBlockChange
		PACKET_LENGTH[0x35] = 12; //BlockChange
		PACKET_LENGTH[0x36] = 14; //BlockAction
		PACKET_LENGTH[0x37] = 17; //BlockBreakAnimation
		PACKET_LENGTH[0x38] = MUST_READ_ALL; //MapChunkBulk
		PACKET_LENGTH[0x3C] = MUST_READ_ALL; //Explosion
		PACKET_LENGTH[0x3D] = 18; //SoundEffect
		PACKET_LENGTH[0x3E] = MUST_READ_ALL; //NamedSoundEffect
		PACKET_LENGTH[0x3F] = MUST_READ_ALL; //Particle
		PACKET_LENGTH[0x46] = 2; //ChangeGameState
		PACKET_LENGTH[0x47] = 17; //SpawnGlobalEntity
		PACKET_LENGTH[0x64] = MUST_READ_ALL; //OpenWindow
		PACKET_LENGTH[0x65] = 1; //CloseWindow
		PACKET_LENGTH[0x66] = MUST_READ_ALL; //ClickWindow
		PACKET_LENGTH[0x67] = MUST_READ_ALL; //SetSlot
		PACKET_LENGTH[0x68] = MUST_READ_ALL; //SetWindowItems
		PACKET_LENGTH[0x69] = 5; //UpdateWindowProperty
		PACKET_LENGTH[0x6A] = 4; //ConfirmTansaction
		PACKET_LENGTH[0x6B] = MUST_READ_ALL; //CreativeInventoryAction
		PACKET_LENGTH[0x6C] = 2; //EnchantItem
		PACKET_LENGTH[0x82] = MUST_READ_ALL; //UpdateSign
		PACKET_LENGTH[0x83] = MUST_READ_ALL; //ItemData
		PACKET_LENGTH[0x84] = MUST_READ_ALL; //UpdateTileEntity
		PACKET_LENGTH[0xC8] = 5; //IncrementStatistic
		PACKET_LENGTH[0xCA] = 3; //PlayerAbilities
		PACKET_LENGTH[0xCB] = MUST_READ_ALL; //TabComplete, TODO: handle on bungee
		PACKET_LENGTH[0xCD] = 1; //ClientStatuses
	}

	public static ByteBuf readPacket(int packetId, ByteBuf buf) {
		int length = PACKET_LENGTH[packetId];
		if (length == NOT_SUPPORTED) {
			throw new IllegalArgumentException("Unknown packet id: "+packetId);
		}
		ByteBuf packetdata = Unpooled.buffer();
		packetdata.writeByte(packetId);
		if (length != MUST_READ_ALL) {
			Utils.rewriteBytes(buf, packetdata, length);
		}
		switch (packetId) {
			case 0x05: { //EntityEquipment
				Utils.rewriteBytes(buf, packetdata, Integer.BYTES + Short.BYTES);
				packetdata.writeBytes(PacketDataSerializer.readItemStackData(buf));
				break;
			}
			case 0x0F: { //PlayerBlockPlace
				Utils.rewriteBytes(buf, packetdata, Integer.BYTES + Byte.BYTES + Integer.BYTES + Byte.BYTES);
				packetdata.writeBytes(PacketDataSerializer.readItemStackData(buf));
				Utils.rewriteBytes(buf, packetdata, Byte.BYTES * 3);
				break;
			}
			case 0x14: { //SpawnNamedEntity
				Utils.rewriteBytes(buf, packetdata, Integer.BYTES);
				PacketDataSerializer.writeString(PacketDataSerializer.readString(buf), packetdata);
				Utils.rewriteBytes(buf, packetdata, Integer.BYTES * 3 + Byte.BYTES * 2 + Short.BYTES);
				packetdata.writeBytes(PacketDataSerializer.readDatawatcherData(buf));
				break;
			}
			case 0x17: { //SpawnObject
				Utils.rewriteBytes(buf, packetdata, Integer.BYTES + Byte.BYTES + Integer.BYTES * 3 + Byte.BYTES * 2);
				int objectdata = buf.readInt();
				packetdata.writeInt(objectdata);
				if (objectdata != 0) {
					Utils.rewriteBytes(buf, packetdata, Short.BYTES * 3);
				}
				break;
			}
			case 0x18: { //SpawnMob
				Utils.rewriteBytes(buf, packetdata, Integer.BYTES + Byte.BYTES + Integer.BYTES * 3 + Byte.BYTES * 3 + Short.BYTES * 3);
				packetdata.writeBytes(PacketDataSerializer.readDatawatcherData(buf));
				break;
			}
			case 0x19: { //SpawnPainting
				Utils.rewriteBytes(buf, packetdata, Integer.BYTES);
				PacketDataSerializer.writeString(PacketDataSerializer.readString(buf), packetdata);
				Utils.rewriteBytes(buf, packetdata, Integer.BYTES * 4);
				break;
			}
			case 0x1D: { //DestroyEntity
				int count = buf.readByte();
				packetdata.writeByte(count);
				Utils.rewriteBytes(buf, packetdata, count * Integer.BYTES);
				break;
			}
			case 0x28: { //EntityMetadata
				Utils.rewriteBytes(buf, packetdata, Integer.BYTES);
				packetdata.writeBytes(PacketDataSerializer.readDatawatcherData(buf));
				break;
			}
			case 0x33: { //ChunkData
				Utils.rewriteBytes(buf, packetdata, Integer.BYTES * 2 + Byte.BYTES + Short.BYTES * 2);
				int size = buf.readInt();
				packetdata.writeInt(size);
				Utils.rewriteBytes(buf, packetdata, size);
				break;
			}
			case 0x34: { //MultiBlockChange
				Utils.rewriteBytes(buf, packetdata, Integer.BYTES * 2 + Short.BYTES);
				int size = buf.readInt();
				packetdata.writeInt(size);
				Utils.rewriteBytes(buf, packetdata, size);
				break;
			}
			case 0x38: { //MapChunkBulk
				int chunkcount = buf.readShort();
				packetdata.writeShort(chunkcount);
				int size = buf.readInt();
				packetdata.writeInt(size);
				packetdata.writeBoolean(buf.readBoolean());
				Utils.rewriteBytes(buf, packetdata, size);
				Utils.rewriteBytes(buf, packetdata, (Integer.BYTES * 2 + Short.BYTES * 2) * chunkcount);
				break;
			}
			case 0x3C: { //Explosion
				Utils.rewriteBytes(buf, packetdata, Double.BYTES * 3 + Float.BYTES);
				int count = buf.readInt();
				packetdata.writeInt(count);
				Utils.rewriteBytes(buf, packetdata, Byte.BYTES * 3 * count);
				Utils.rewriteBytes(buf, packetdata, Float.BYTES * 3);
				break;
			}
			case 0x3E: { //NamedSoundEffect
				PacketDataSerializer.writeString(PacketDataSerializer.readString(buf), packetdata);
				Utils.rewriteBytes(buf, packetdata, Integer.BYTES * 3 + Float.BYTES + Byte.BYTES);
				break;
			}
			case 0x3F: { //Particle
				PacketDataSerializer.writeString(PacketDataSerializer.readString(buf), packetdata);
				Utils.rewriteBytes(buf, packetdata, Float.BYTES * 7 + Integer.BYTES);
				break;
			}
			case 0x64: { //OpenWindow
				Utils.rewriteBytes(buf, packetdata, Byte.BYTES * 2);
				PacketDataSerializer.writeString(PacketDataSerializer.readString(buf), packetdata);
				Utils.rewriteBytes(buf, packetdata, Byte.BYTES * 2);
				break;
			}
			case 0x66: { //ClickWindow
				Utils.rewriteBytes(buf, packetdata, Byte.BYTES + Short.BYTES + Byte.BYTES + Short.BYTES + Byte.BYTES);
				packetdata.writeBytes(PacketDataSerializer.readItemStackData(buf));
				break;
			}
			case 0x67: { //SetSlot
				Utils.rewriteBytes(buf, packetdata, Byte.BYTES + Short.BYTES);
				packetdata.writeBytes(PacketDataSerializer.readItemStackData(buf));
				break;
			}
			case 0x68: { //SetWindowItems
				Utils.rewriteBytes(buf, packetdata, Byte.BYTES);
				int count = buf.readShort();
				packetdata.writeShort(count);
				for (int i = 0; i < count; i++) {
					packetdata.writeBytes(PacketDataSerializer.readItemStackData(buf));
				}
				break;
			}
			case 0x6B: { //CreativeInventoryAction
				Utils.rewriteBytes(buf, packetdata, Short.BYTES);
				packetdata.writeBytes(PacketDataSerializer.readItemStackData(buf));
				break;
			}
			case 0x82: { //UpdateSign
				Utils.rewriteBytes(buf, packetdata, Integer.BYTES + Short.BYTES + Integer.BYTES);
				PacketDataSerializer.writeString(PacketDataSerializer.readString(buf), packetdata);
				PacketDataSerializer.writeString(PacketDataSerializer.readString(buf), packetdata);
				PacketDataSerializer.writeString(PacketDataSerializer.readString(buf), packetdata);
				PacketDataSerializer.writeString(PacketDataSerializer.readString(buf), packetdata);
				break;
			}
			case 0x83: { //ItemData
				Utils.rewriteBytes(buf, packetdata, Short.BYTES * 2);
				int size = buf.readShort();
				packetdata.writeShort(size);
				Utils.rewriteBytes(buf, packetdata, size);
				break;
			}
			case 0x84: { //UpdateTileEntity
				Utils.rewriteBytes(buf, packetdata, Integer.BYTES + Short.BYTES + Integer.BYTES + Byte.BYTES);
				int size = buf.readShort();
				packetdata.writeShort(size);
				Utils.rewriteBytes(buf, packetdata, size);
				break;
			}
			case 0xCB: { //TabComplete
				PacketDataSerializer.writeString(PacketDataSerializer.readString(buf), packetdata);
				break;
			}
			default: {
				break;
			}
		}
		return packetdata;
	}

}
