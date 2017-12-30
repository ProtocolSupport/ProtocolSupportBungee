package protocolsupport.protocol.packet.middleimpl.readable.login.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.EncryptionResponse;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.ArraySerializer;

public class EncryptionResponsePacket extends LegacyDefinedReadableMiddlePacket {

	public static final int PACKET_ID = 0xFC;

	public EncryptionResponsePacket() {
		super(PACKET_ID);
	}

	protected byte[] sharedSecret;
	protected byte[] verifyToken;

	@Override
	protected void read0(ByteBuf from) {
		sharedSecret = ArraySerializer.readShortLengthByteArray(from);
		verifyToken = ArraySerializer.readShortLengthByteArray(from);
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(new EncryptionResponse(sharedSecret, verifyToken), Unpooled.wrappedBuffer(readbytes)));
	}

}
