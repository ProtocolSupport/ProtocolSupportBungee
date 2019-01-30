package protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Login;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middle.WriteableMiddlePacket;
import protocolsupport.protocol.serializer.PEPacketIdSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.utils.netty.Allocator;

public class StartGamePacket extends WriteableMiddlePacket<Login> {

	@Override
	public Collection<ByteBuf> toData(Login packet) {
		ProtocolVersion version = connection.getVersion();
		ArrayList<ByteBuf> packets = new ArrayList<>();
		ByteBuf resourcepack = Allocator.allocateBuffer();
		PEPacketIdSerializer.writePacketId(resourcepack, PEPacketId.Clientbound.PLAY_RESOURCE_PACK);
		resourcepack.writeBoolean(false); // required
		resourcepack.writeShortLE(0); //beh packs count
		if (version.isAfterOrEq(ProtocolVersion.MINECRAFT_PE_1_9)) {
			resourcepack.writeBoolean(false); // ???
		}
		resourcepack.writeShortLE(0); //res packs count
		if (version.isAfterOrEq(ProtocolVersion.MINECRAFT_PE_1_9)) {
			resourcepack.writeBoolean(false); // ???
		}
		packets.add(resourcepack);
		ByteBuf resourcestack = Allocator.allocateBuffer();
		PEPacketIdSerializer.writePacketId(resourcestack, PEPacketId.Clientbound.PLAY_RESOURCE_STACK);
		resourcestack.writeBoolean(false); // required
		VarNumberSerializer.writeVarInt(resourcestack, 0); //beh packs count
		VarNumberSerializer.writeVarInt(resourcestack, 0); //res packs count
		VarNumberSerializer.writeVarInt(resourcestack, 0); //?
		VarNumberSerializer.writeVarInt(resourcestack, 0); //?
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
		startgame.writeBytes(cache.getStartGameData()); // stash the rest of the packet
		packets.add(startgame);
		cache.setStartGameData(null); //clear cache
		return packets;
	}

}
