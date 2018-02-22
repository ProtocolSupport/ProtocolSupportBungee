package protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe;

import java.text.MessageFormat;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Respawn;
import protocolsupport.protocol.packet.middleimpl.writeable.PESingleWriteablePacket;
import protocolsupport.protocol.serializer.VarNumberSerializer;

public class RespawnPacket extends PESingleWriteablePacket<Respawn> {

	public RespawnPacket() {
		super(61);
	}

	@Override
	protected void write(ByteBuf data, Respawn packet) {
		VarNumberSerializer.writeSVarInt(data, getPeDimensionId(packet.getDimension()));
		data.writeFloatLE(0); //x
		data.writeFloatLE(0); //y
		data.writeFloatLE(0); //z
		data.writeBoolean(true); //respawn
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

}
