package protocolsupport.protocol.packet.middleimpl.readable.play.v_pe;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Respawn;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.PEDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.VarNumberSerializer;

public class RespawnPacket extends PEDefinedReadableMiddlePacket {

	public RespawnPacket() {
		super(PEPacketId.Clientbound.PLAY_RESPAWN);
	}

	protected int dimensionId;

	@Override
	protected void read0(ByteBuf from) {
		dimensionId = getPcDimensionId(VarNumberSerializer.readSVarInt(from));
		from.readFloatLE(); //x
		from.readFloatLE(); //y
		from.readFloatLE(); //z
		from.readBoolean(); //respawn
		cache.setRealDimension(dimensionId);
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new Respawn(dimensionId, (short) 0, (short) 0, ""), Unpooled.wrappedBuffer(readbytes)));
	}

	public static int getPcDimensionId(int dimId) {
		switch (dimId) {
			case 1: {
				return -1;
			}
			case 2: {
				return 1;
			}
			case 0: {
				return 0;
			}
			default: {
				throw new IllegalArgumentException(MessageFormat.format("Uknown dim id {0}", dimId));
			}
		}
	}

}
