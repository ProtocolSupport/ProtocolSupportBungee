package protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Login;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middle.WriteableMiddlePacket;
import protocolsupport.protocol.serializer.PEPacketIdSerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.utils.netty.Allocator;

public class StartGamePacket extends WriteableMiddlePacket<Login> {

	private static final String levelId = UUID.randomUUID().toString();

	@Override
	public Collection<ByteBuf> toData(Login packet) {
		ArrayList<ByteBuf> packets = new ArrayList<>();
		ByteBuf resourcepack = Allocator.allocateBuffer();
		PEPacketIdSerializer.writePacketId(resourcepack, PEPacketId.Clientbound.PLAY_RESOURCE_PACK);
		resourcepack.writeBoolean(false); // required
		resourcepack.writeShortLE(0); //beh packs count
		resourcepack.writeShortLE(0); //res packs count
		packets.add(resourcepack);
		ByteBuf resourcestack = Allocator.allocateBuffer();
		PEPacketIdSerializer.writePacketId(resourcestack, PEPacketId.Clientbound.PLAY_RESOURCE_STACK);
		resourcestack.writeBoolean(false); // required
		VarNumberSerializer.writeVarInt(resourcestack, 0); //beh packs count
		VarNumberSerializer.writeVarInt(resourcestack, 0); //res packs count
		packets.add(resourcestack);
		ByteBuf startgame = Allocator.allocateBuffer();
		PEPacketIdSerializer.writePacketId(startgame, PEPacketId.Clientbound.PLAY_START_GAME);
		VarNumberSerializer.writeSVarLong(startgame, packet.getEntityId());
		VarNumberSerializer.writeVarLong(startgame, packet.getEntityId());
		VarNumberSerializer.writeSVarInt(startgame, packet.getGameMode());
		startgame.writeFloatLE(0); //player x
		startgame.writeFloatLE(0); //player y
		startgame.writeFloatLE(0); //player z
		startgame.writeFloatLE(0); //player pitch
		startgame.writeFloatLE(0); //player yaw
		VarNumberSerializer.writeSVarInt(startgame, 0); //seed
		VarNumberSerializer.writeSVarInt(startgame, RespawnPacket.getPeDimensionId(packet.getDimension())); //world dimension
		VarNumberSerializer.writeSVarInt(startgame, 1); //world type (1 - infinite)
		VarNumberSerializer.writeSVarInt(startgame, 0); //world gamemode
		VarNumberSerializer.writeSVarInt(startgame, packet.getDifficulty()); //world difficulty
		VarNumberSerializer.writeSVarInt(startgame, 0); //world spawn x
		VarNumberSerializer.writeVarInt(startgame, 0); //world spawn y
		VarNumberSerializer.writeSVarInt(startgame, 0); //world spawn z
		startgame.writeBoolean(false); //disable achievements
		VarNumberSerializer.writeSVarInt(startgame, 0); //time
		startgame.writeBoolean(false); //edu mode
		startgame.writeBoolean(false); //edu features
		startgame.writeFloatLE(0); //rain level
		startgame.writeFloatLE(0); //lighting level
		startgame.writeBoolean(true); //is multiplayer
		startgame.writeBoolean(false); //broadcast to lan
		startgame.writeBoolean(false); //broadcast to xbl
		startgame.writeBoolean(true); //commands enabled
		startgame.writeBoolean(false); //needs texture pack
		VarNumberSerializer.writeVarInt(startgame, 0); //game rules
		startgame.writeBoolean(false); //bonus chest
		startgame.writeBoolean(false); //player map enabled
		startgame.writeBoolean(false); //trust players
		VarNumberSerializer.writeSVarInt(startgame, 1); //permission level
		VarNumberSerializer.writeSVarInt(startgame, 4); //game publish setting
		startgame.writeIntLE(4); //Server chunk tick radius..
		startgame.writeBoolean(false); //Platformbroadcast
		VarNumberSerializer.writeVarInt(startgame, 0); //Broadcast mode
		startgame.writeBoolean(false); //Broadcast intent
		startgame.writeBoolean(false); //hasLockedRes pack
		startgame.writeBoolean(false); //hasLockedBeh pack
		startgame.writeBoolean(false); //hasLocked world template.
		startgame.writeBoolean(false); //Microsoft GamerTags only. Hell no!
		StringSerializer.writeVarIntUTF8String(startgame, levelId);
		StringSerializer.writeVarIntUTF8String(startgame, ""); //level name (will packet.getLevelType() work?)
		StringSerializer.writeVarIntUTF8String(startgame, ""); //template pack id
		startgame.writeBoolean(false); //is trial
		startgame.writeLongLE(0); //world ticks
		VarNumberSerializer.writeSVarInt(startgame, 0); //enchantment seed FFS MOJANG
		startgame.writeBytes(cache.getPeRuntimeIDs()); // this also has the version name afterwards, just being lazy
		//StringSerializer.writeVarIntUTF8String(startgame, version.getName()); //combined with ids above

		cache.setPeRuntimeIDs(null); //clear cache

		packets.add(startgame);
		return packets;
	}

}
