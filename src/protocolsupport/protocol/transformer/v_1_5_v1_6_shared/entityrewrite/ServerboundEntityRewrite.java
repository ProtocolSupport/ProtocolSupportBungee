package protocolsupport.protocol.transformer.v_1_5_v1_6_shared.entityrewrite;

import io.netty.buffer.ByteBuf;

public class ServerboundEntityRewrite {

	public void rewriteServerbound(ByteBuf buf, int oldId, int newId) {
		int readerIndex = buf.readerIndex();
		int packetId = buf.readByte() & 0xFF;
		switch (packetId) {
			case 0x07: { //UseEntity
				rewriteInt(buf, oldId, newId, 1);
				break;
			}
			case 0x13: { //EntityAction
				rewriteInt(buf, oldId, newId, 1);
				break;
			}
		}
		buf.readerIndex(readerIndex);
	}

	protected static void rewriteInt(final ByteBuf packet, final int oldId, final int newId, final int offset) {
		final int readId = packet.getInt(offset);
		if (readId == oldId) {
			packet.setInt(offset, newId);
		} else if (readId == newId) {
			packet.setInt(offset, oldId);
		}
	}

}
