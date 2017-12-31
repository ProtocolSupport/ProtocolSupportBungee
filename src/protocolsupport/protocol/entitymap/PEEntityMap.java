package protocolsupport.protocol.entitymap;

import io.netty.buffer.ByteBuf;

//TODO:
public class PEEntityMap extends EntityMap {

	@Override
	public void rewriteClientbound(ByteBuf buf, int oldId, int newId) {
	}

	@Override
	public void rewriteServerbound(ByteBuf buf, int oldId, int newId) {
	}

}
