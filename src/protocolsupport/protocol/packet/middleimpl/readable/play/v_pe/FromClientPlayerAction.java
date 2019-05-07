package protocolsupport.protocol.packet.middleimpl.readable.play.v_pe;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.PluginMessage;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.PEDefinedReadableMiddlePacket;
import protocolsupport.protocol.pipeline.version.v_pe.NoopDefinedPacket;
import protocolsupport.protocol.serializer.VarNumberSerializer;

public class FromClientPlayerAction extends PEDefinedReadableMiddlePacket {

	protected static final int DIMENSION_CHANGE_ACK = 14;

	protected int action;

	public FromClientPlayerAction() {
		super(PEPacketId.Serverbound.PLAY_PLAYER_ACTION);
	}

	@Override
	protected void read0(ByteBuf from) {
		VarNumberSerializer.readVarLong(from); //entity id
		action = VarNumberSerializer.readSVarInt(from);
		VarNumberSerializer.readSVarInt(from); //block x
		VarNumberSerializer.readVarInt(from); //y
		VarNumberSerializer.readSVarInt(from); //z
		VarNumberSerializer.readSVarInt(from); //face
		from.skipBytes(from.readableBytes());

		switch (action) {
			case DIMENSION_CHANGE_ACK: {
				// player is 'alive' again, lets unlock our queue to get things moving
				connection.sendPacketToClient(new PluginMessage("ps:bungeeunlock", new byte[0], false));
				return;
			}
			default:
		}
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new NoopDefinedPacket(), Unpooled.wrappedBuffer(readbytes)));
	}

}
