package protocolsupport.protocol.packet.middleimpl.writeable.play.v_4_5_6;

import java.util.ArrayList;
import java.util.Collection;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import net.md_5.bungee.protocol.packet.PlayerListItem.Action;
import net.md_5.bungee.protocol.packet.PlayerListItem.Item;
import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middle.WriteableMiddlePacket;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.utils.netty.Allocator;

public class PlayerListItemPacket extends WriteableMiddlePacket<PlayerListItem> {

	@Override
	public Collection<ByteBuf> toData(PlayerListItem packet) {
		ArrayList<ByteBuf> packets = new ArrayList<ByteBuf>();
		for (Item item : packet.getItems()) {
			ByteBuf data = Allocator.allocateBuffer();
			data.writeByte(LegacyPacketId.Clientbound.PLAY_PLAYER_LIST);
            StringSerializer.writeShortUTF16BEString(data, getDisplayName(item));
            data.writeBoolean(packet.getAction() != Action.REMOVE_PLAYER);
            data.writeShort(item.getPing());
            packets.add(data);
		}
		return packets;
	}

	private static String getDisplayName(Item item) {
		String name = item.getDisplayName();
		if (name == null) {
			name = "Unknown";
		}
		return name;
	}

}
