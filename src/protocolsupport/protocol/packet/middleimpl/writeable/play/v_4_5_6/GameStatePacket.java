package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.GameState;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.writeable.LegacySingleWriteablePacket;

public class GameStatePacket extends LegacySingleWriteablePacket<GameState> {

	public GameStatePacket() {
		super(LegacyPacketId.Clientbound.PLAY_GAME_STATE);
	}

	@Override
	protected void write(ByteBuf data, GameState packet) {
		data.writeByte(packet.getState());
		data.writeByte((int) packet.getValue());
	}

}
