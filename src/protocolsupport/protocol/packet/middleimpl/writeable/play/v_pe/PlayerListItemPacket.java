package protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import protocolsupport.protocol.packet.middle.WriteableMiddlePacket;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.utils.netty.Allocator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class PlayerListItemPacket extends WriteableMiddlePacket<PlayerListItem> {

    @Override
    public Collection<ByteBuf> toData(PlayerListItem origin) {
        ByteBuf buf = cache.tabList().remove(origin);
        if (!(buf == null)) {
            return Collections.singletonList(buf);
        }
        switch (origin.getAction()) {
            case ADD_PLAYER:
            case UPDATE_GAMEMODE:
            case UPDATE_LATENCY:
            case UPDATE_DISPLAY_NAME:
                return Collections.emptyList();
            case REMOVE_PLAYER:
                return remove(origin.getItems());
        }
        throw new IllegalStateException("action type");
    }

    public static Collection<ByteBuf> remove(PlayerListItem.Item[] input) {
        ByteBuf pk = Allocator.allocateBuffer();
        VarNumberSerializer.writeVarInt(pk, 63);
        pk.writeByte(0);
        pk.writeByte(0);
        pk.writeByte(1);// action remove
        VarNumberSerializer.writeVarInt(pk, input.length);
        Arrays.asList(input).forEach(ele -> MiscSerializer.writeUUIDLE(pk, ele.getUuid()));
        return Collections.singletonList(pk);
    }
}
