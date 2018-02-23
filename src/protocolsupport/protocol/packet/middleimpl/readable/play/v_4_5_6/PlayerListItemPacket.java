package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import net.md_5.bungee.protocol.packet.PlayerListItem.Action;
import net.md_5.bungee.protocol.packet.PlayerListItem.Item;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.StringSerializer;

public class PlayerListItemPacket extends LegacyDefinedReadableMiddlePacket {

	public PlayerListItemPacket() {
		super(LegacyPacketId.Clientbound.PLAY_PLAYER_LIST);
	}

	protected Action action;
	protected Item[] items;

	@Override
	protected void read0(ByteBuf from) {
		String username = StringSerializer.readShortUTF16BEString(from);
		boolean add = from.readBoolean();
		int ping = from.readShort();
		action = add ? Action.ADD_PLAYER : Action.REMOVE_PLAYER;
		Item item = new Item();
		item.setUsername(username);
		item.setDisplayName(username);
		item.setGamemode(0);
		item.setPing(ping);
		items = new Item[] { item };
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		PlayerListItem packet = new PlayerListItem();
		packet.setAction(action);
		packet.setItems(items);
		return Collections.singletonList(new PacketWrapper(packet, Unpooled.wrappedBuffer(readbytes)));
	}

}
