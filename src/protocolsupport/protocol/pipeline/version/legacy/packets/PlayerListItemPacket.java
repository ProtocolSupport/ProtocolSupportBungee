package protocolsupport.protocol.pipeline.version.legacy.packets;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import protocolsupport.protocol.serializer.LegacySerializer;
import protocolsupport.utils.Utils;

//TODO: some cache to make it work with not real players
public class PlayerListItemPacket extends PlayerListItem implements TransformedPacket {

	private Action action;
	private Item[] items;

	public PlayerListItemPacket() {
	}

	public PlayerListItemPacket(Action action, Item item) {
		this.action = action;
		this.items = new Item[] { item };
		if (this.items[0].getDisplayName() == null) {
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(this.items[0].getUuid());
			this.items[0].setDisplayName(player != null ? player.getDisplayName() : "Unknown");
		}
	}

	@Override
	public void read(ByteBuf buf) {
		String username = LegacySerializer.readString(buf);
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(username);
		boolean add = buf.readBoolean();
		int ping = buf.readShort();
		action = add ? Action.ADD_PLAYER : Action.REMOVE_PLAYER;
		Item item = new Item();
		item.setDisplayName(username);
		item.setUuid(player != null ? player.getUniqueId() : UUID.nameUUIDFromBytes(username.getBytes(StandardCharsets.UTF_8)));
		item.setGamemode(0);
		item.setPing(ping);
		items = new Item[] { item };
	}

	@Override
	public void write(ByteBuf buf) {
		LegacySerializer.writeString(Utils.clampString(items[0].getDisplayName(), 16), buf);
		buf.writeBoolean((action == Action.ADD_PLAYER) || (action == Action.UPDATE_DISPLAY_NAME));
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
	public int getId() {
		return 0xC9;
	}

}
