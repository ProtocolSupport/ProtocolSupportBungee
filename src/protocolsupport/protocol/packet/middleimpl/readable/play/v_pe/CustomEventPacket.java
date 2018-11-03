package protocolsupport.protocol.packet.middleimpl.readable.play.v_pe;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.PluginMessage;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.PEDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.StringSerializer;

import java.util.Arrays;
import java.util.Collection;

public class CustomEventPacket extends PEDefinedReadableMiddlePacket {
    public CustomEventPacket() {
        super(PEPacketId.Dualbound.CUSTOM_EVENT);
    }

    String tag;
    byte[] data;

    @Override
    protected void read0(ByteBuf from) {
        tag = StringSerializer.readVarIntUTF8String(from);
        data = MiscSerializer.readAllBytes(from);
    }

    @Override
    public Collection<PacketWrapper> toNative() {
        return Arrays.asList(
            new PacketWrapper(new PluginMessage(tag, data, false), Unpooled.wrappedBuffer(readbytes))
        );
    }
}
