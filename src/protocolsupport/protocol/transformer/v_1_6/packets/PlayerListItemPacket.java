package protocolsupport.protocol.transformer.v_1_6.packets;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.transformer.TransformedPacket;
import protocolsupport.protocol.transformer.v_1_5.PacketDataSerializer;

public class PlayerListItemPacket extends PlayerListItem implements TransformedPacket {

	private Action action;
	private Item[] items;

	public PlayerListItemPacket() {
	}

	public PlayerListItemPacket(Action action, Item item) {
		this.action = action;
		this.items = new Item[] { item };
	}

	@Override
	public void read(ByteBuf buf) {
		String username = PacketDataSerializer.readString(buf);
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(username);
		boolean add = buf.readBoolean();
		int ping = buf.readShort();
		action = add ? Action.ADD_PLAYER : Action.REMOVE_PLAYER;
		PlayerListItem.Item item = new PlayerListItem.Item();
		item.setDisplayName(username);
		item.setUuid(player != null ? player.getUniqueId() : UUID.nameUUIDFromBytes(username.getBytes(StandardCharsets.UTF_8)));
		item.setGamemode(0);
		item.setPing(ping);
		items = new PlayerListItem.Item[] { item };
	}

	@Override
	public void write(ByteBuf buf) {
		PacketDataSerializer.writeString(items[0].getDisplayName(), buf);
		buf.writeBoolean(action == Action.ADD_PLAYER);
		buf.writeShort(items[0].getPing());
	}

	@Override
	public void handle(final AbstractPacketHandler handler) throws Exception {
		super.handle(handler);
	}

	@Override
	public Action getAction() {
		return this.action;
	}

	@Override
	public Item[] getItems() {
		return this.items;
	}

	@Override
	public void setAction(final Action action) {
		this.action = action;
	}

	@Override
	public void setItems(final Item[] items) {
		this.items = items;
	}

	@Override
	public boolean shouldWrite() {
		return true;
	}

	@Override
	public int getId() {
		return 0xC9;
	}

}
