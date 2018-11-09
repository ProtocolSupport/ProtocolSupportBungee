package protocolsupport.protocol.packet.middleimpl.readable.play.v_pe;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.PluginMessage;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.PEDefinedReadableMiddlePacket;
import protocolsupport.protocol.pipeline.version.v_pe.NoopDefinedPacket;

import java.util.Collection;
import java.util.Collections;

public class FromClientPlayerLook extends PEDefinedReadableMiddlePacket {
    public FromClientPlayerLook() {
        super(PEPacketId.Dualbound.PLAY_PLAYER_MOVE_LOOK);
    }

    @Override
    protected void read0(ByteBuf from) {
        from.skipBytes(from.readableBytes());
        // player is 'alive' again, lets unlock our queue to get things moving
        if (cache.isStashingClientPackets()) {
            connection.sendPacketToClient(new PluginMessage("ps:bungeeunlock", new byte[0], false));
        }
    }

    @Override
    public Collection<PacketWrapper> toNative() {
        return Collections.singletonList(new PacketWrapper(new NoopDefinedPacket(), Unpooled.wrappedBuffer(readbytes)));
    }
}
