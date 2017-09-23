package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import net.md_5.bungee.protocol.packet.PlayerListItem.Action;
import net.md_5.bungee.protocol.packet.PlayerListItem.Item;
import protocolsupport.protocol.packet.middleimpl.readable.DefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class PlayerListItemPacket extends DefinedReadableMiddlePacket {

	public static final int PACKET_ID = 0xC9;

	public PlayerListItemPacket() {
		super(PACKET_ID);
	}

	protected Action action;
	protected Item[] items;

	@Override
	protected void read0(ByteBuf from) {
		String username = LegacySerializer.readString(from);
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
