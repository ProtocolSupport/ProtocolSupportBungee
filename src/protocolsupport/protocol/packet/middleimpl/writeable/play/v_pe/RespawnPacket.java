package protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.packet.Respawn;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middleimpl.writeable.PESingleWriteablePacket;
import protocolsupport.protocol.serializer.ArraySerializer;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.PEPacketIdSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;

public class RespawnPacket extends PESingleWriteablePacket<Respawn> {

	public RespawnPacket() {
		super(PEPacketId.Clientbound.PLAY_RESPAWN);
	}

	@Override
	protected void write(ByteBuf data, Respawn packet) {
		VarNumberSerializer.writeSVarInt(data, getPeDimensionId(packet.getDimension()));
		data.writeFloatLE(0); //x
		data.writeFloatLE(0); //y
		data.writeFloatLE(0); //z
		data.writeBoolean(true); //respawn
	}

	@Override
	public Collection<ByteBuf> toData(Respawn packet) {
		ByteBuf single = super.toData(packet).iterator().next();
		ArrayList<ByteBuf> packets = new ArrayList<>();
		packets.add(single);
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				ByteBuf buffer = Unpooled.buffer();
				PEPacketIdSerializer.writePacketId(buffer, PEPacketId.Clientbound.PLAY_CHUNK_DATA);
				VarNumberSerializer.writeSVarInt(buffer, x);
				VarNumberSerializer.writeSVarInt(buffer, z);
				buffer.writeBytes(getPEChunkData());
				packets.add(buffer);
			}
		}
		return packets;
	}

	public static int getPeDimensionId(int dimId) {
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
				throw new IllegalArgumentException(MessageFormat.format("Unknown dim id {0}", dimId));
			}
		}
	}

	private static byte[] fakePEChunkData;
	static {
		ByteBuf serializer = Unpooled.buffer();
		ArraySerializer.writeVarIntByteArray(serializer, chunkdata -> {
			chunkdata.writeByte(1); //1 section
			chunkdata.writeByte(8); //New subchunk version!
			chunkdata.writeByte(1); //Zero blockstorages :O
			chunkdata.writeByte((1 << 1) | 1);  //Runtimeflag and palette id.
			chunkdata.writeZero(512);
			VarNumberSerializer.writeSVarInt(chunkdata, 1); //Palette size
			VarNumberSerializer.writeSVarInt(chunkdata, 0); //Air
			chunkdata.writeZero(512); //heightmap.
			chunkdata.writeZero(256); //Biomedata.
			chunkdata.writeByte(0); //borders
		});
		fakePEChunkData = MiscSerializer.readAllBytes(serializer);
	}
	public static byte[] getPEChunkData() {
		return fakePEChunkData;
	}
}
