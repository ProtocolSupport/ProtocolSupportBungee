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
        super(PEPacketId.Clientbound.CUSTOM_EVENT);
    }

    String tag;
    byte[] data;

    @Override
    protected void read0(ByteBuf from) {
        tag = StringSerializer.readVarIntUTF8String(from);
        data = MiscSerializer.readAllBytes(from);

        System.out.println("CustomEventPacket " + tag);
    }

    @Override
    public Collection<PacketWrapper> toNative() {
        PluginMessage pluginMessage = new PluginMessage();

        pluginMessage.setTag(tag);
        pluginMessage.setData(data);

        return Arrays.asList(
            new PacketWrapper(pluginMessage, Unpooled.wrappedBuffer(readbytes))
        );
    }
}
