package protocolsupport.protocol.packet.middleimpl.readable.play.v_pe;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.PEDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlayerListItemPacket extends PEDefinedReadableMiddlePacket {

    private Action action;
    private List<UUID> elementlist;

    public PlayerListItemPacket() {
        super(PEPacketId.Clientbound.PLAY_PLAYER_INFO);
    }

    @Override
    protected void read0(ByteBuf pk) {
        byte actionid = pk.readByte();
        action = Action.values()[actionid];

        int len = Math.toIntExact(VarNumberSerializer.readVarInt(pk));
        elementlist = new ArrayList<>();
        for (int i = len; i >= 1; i--) {
            elementlist.add(action.read(pk));
        }
    }

    @Override
    public Collection<PacketWrapper> toNative() {
        PlayerListItem pk = tonative();
        ByteBuf buf = Unpooled.wrappedBuffer(readbytes);
        cache.tabList().put(pk, buf);
        return Collections.singletonList(new PacketWrapper(pk, Unpooled.EMPTY_BUFFER));
    }

    private PlayerListItem tonative() {
        PlayerListItem item = new PlayerListItem();
        item.setAction(action.origin);
        item.setItems(elementlist.stream().map(el -> {
            PlayerListItem.Item i = new PlayerListItem.Item();
            i.setUuid(el);
            return i;
        }).toArray(PlayerListItem.Item[]::new));
        return item;
    }

    enum Action {
        ADD_PLAYER(PlayerListItem.Action.ADD_PLAYER) {
            UUID read(ByteBuf buf) {
                UUID id = MiscSerializer.readUUIDLE(buf);
                VarNumberSerializer.readVarInt(buf);// Entity id
                StringSerializer.readVarIntUTF8String(buf);
                StringSerializer.readVarIntUTF8String(buf);
                StringSerializer.readVarIntUTF8String(buf);
                StringSerializer.readVarIntUTF8String(buf);
                StringSerializer.readVarIntUTF8String(buf);
                StringSerializer.readVarIntUTF8String(buf);
                StringSerializer.readVarIntUTF8String(buf);
                return id;
            }
        },
        REMOVE_PLAYER(PlayerListItem.Action.REMOVE_PLAYER) {
            UUID read(ByteBuf buf) {
                return MiscSerializer.readUUIDLE(buf);
            }
        };

        private final PlayerListItem.Action origin;

        Action(PlayerListItem.Action origin) {
            this.origin = origin;
        }

        UUID read(ByteBuf buf) {
            throw new AbstractMethodError();
        }
    }
}
