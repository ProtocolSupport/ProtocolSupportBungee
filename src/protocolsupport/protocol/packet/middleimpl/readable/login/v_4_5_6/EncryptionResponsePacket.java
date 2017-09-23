package protocolsupport.protocol.packet.middleimpl.readable.login.v_4_5_6;

import java.util.Collection;
import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.EncryptionResponse;
import protocolsupport.protocol.packet.middleimpl.readable.DefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.LegacySerializer;

public class EncryptionResponsePacket extends DefinedReadableMiddlePacket {

	public static final int PACKET_ID = 0xFC;

	public EncryptionResponsePacket() {
		super(PACKET_ID);
	}

	protected byte[] sharedSecret;
	protected byte[] verifyToken;

	@Override
	protected void read0(ByteBuf from) {
		sharedSecret = LegacySerializer.readArray(from);
		verifyToken = LegacySerializer.readArray(from);
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Collections.singletonList(new PacketWrapper(
			new EncryptionResponse(sharedSecret, verifyToken) {
				@Override
				public void handle(AbstractPacketHandler handler) throws Exception {
					((InitialHandler) handler).unsafe().sendPacket(new FakeToClientEncrpytionResponse());
					handler.handle(this);
				}
			},
			Unpooled.wrappedBuffer(readbytes)
		));
	}

	public static class FakeToClientEncrpytionResponse extends DefinedPacket {
		@Override
		public String toString() {
			return getClass().getSimpleName()+"()";
		}
		@Override
		public void handle(AbstractPacketHandler handler) throws Exception {
		}
		@Override
		public int hashCode() {
			return 0;
		}
		@Override
		public boolean equals(Object other) {
			return other instanceof FakeToClientEncrpytionResponse;
		}
	}

}
