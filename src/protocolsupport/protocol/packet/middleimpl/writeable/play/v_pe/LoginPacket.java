package protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Login;
import protocolsupport.protocol.packet.middle.WriteableMiddlePacket;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.utils.netty.Allocator;

public class LoginPacket extends WriteableMiddlePacket<Login> {

	private static final String levelId = UUID.randomUUID().toString();

	private static int getPeDimensionId(int dimId) {
		switch (dimId) {
			case -1: {
				return 1;
			}
			case 1: {
				return 2;
			}
			case 0: {
				return 0;
			}
			default: {
				throw new IllegalArgumentException(MessageFormat.format("Uknown dim id {0}", dimId));
			}
		}
	}

	@Override
	public Collection<ByteBuf> toData(Login packet) {
		ArrayList<ByteBuf> packets = new ArrayList<>();
		ByteBuf resourcepack = Allocator.allocateBuffer();
		writePacketId(resourcepack, 6);
		resourcepack.writeBoolean(false); // required
		resourcepack.writeShortLE(0); //beh packs count
		resourcepack.writeShortLE(0); //res packs count
		packets.add(resourcepack);
		ByteBuf resourcestack = Allocator.allocateBuffer();
		writePacketId(resourcestack, 7);
		resourcestack.writeBoolean(false); // required
		VarNumberSerializer.writeVarInt(resourcestack, 0); //beh packs count
		VarNumberSerializer.writeVarInt(resourcestack, 0); //res packs count
		packets.add(resourcestack);
		ByteBuf startgame = Allocator.allocateBuffer();
		writePacketId(startgame, 11);
		VarNumberSerializer.writeSVarLong(startgame, packet.getEntityId());
		VarNumberSerializer.writeVarLong(startgame, packet.getEntityId());
		VarNumberSerializer.writeSVarInt(startgame, packet.getGameMode());
		MiscSerializer.writeLFloat(startgame, 0); //x
		MiscSerializer.writeLFloat(startgame, 0); //y
		MiscSerializer.writeLFloat(startgame, 0); //z
		MiscSerializer.writeLFloat(startgame, 0); //yaw
		MiscSerializer.writeLFloat(startgame, 0); //pitch
		VarNumberSerializer.writeSVarInt(startgame, 0); //seed
		VarNumberSerializer.writeSVarInt(startgame, getPeDimensionId(packet.getDimension()));
		VarNumberSerializer.writeSVarInt(startgame, 1); //world type (1 - infinite)
		VarNumberSerializer.writeSVarInt(startgame, 0); // world gamemode (SURVIVAL)
		VarNumberSerializer.writeSVarInt(startgame, packet.getDifficulty());
		VarNumberSerializer.writeSVarInt(startgame, 0); //world spawn x
		VarNumberSerializer.writeVarInt(startgame, 0); //world spawn y
		VarNumberSerializer.writeSVarInt(startgame, 0); //world spawn z
		startgame.writeBoolean(false); //disable achievements
		VarNumberSerializer.writeSVarInt(startgame, 0); //time
		startgame.writeBoolean(false); //edu mode
		MiscSerializer.writeLFloat(startgame, 0); //rain level
		MiscSerializer.writeLFloat(startgame, 0); //lighting level
		startgame.writeBoolean(true); //is multiplayer
		startgame.writeBoolean(false); //broadcast to lan
		startgame.writeBoolean(false); //broadcast to xbl
		startgame.writeBoolean(true); //commands enabled
		startgame.writeBoolean(false); //needs texture pack
		VarNumberSerializer.writeVarInt(startgame, 0); //game rules
		startgame.writeBoolean(false); //bonus chest enabled
		startgame.writeBoolean(false); //trust players
		VarNumberSerializer.writeSVarInt(startgame, 1); //permission level
		VarNumberSerializer.writeSVarInt(startgame, 4); //game publish setting
		StringSerializer.writeVarIntUTF8String(startgame, levelId);
		StringSerializer.writeVarIntUTF8String(startgame, ""); //level name (will packet.getLevelType() work?)
		StringSerializer.writeVarIntUTF8String(startgame, ""); //template pack id
		startgame.writeBoolean(false); //is trial
		startgame.writeLong(0); //level time
		VarNumberSerializer.writeSVarInt(startgame, 0); //enchantment seed
		packets.add(startgame);
		return packets;
	}

	protected void writePacketId(ByteBuf data, int packetId) {
		VarNumberSerializer.writeVarInt(data, packetId);
		data.writeByte(0);
		data.writeByte(0);
	}

}
