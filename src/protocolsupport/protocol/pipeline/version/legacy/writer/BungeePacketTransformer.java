package protocolsupport.protocol.pipeline.version.legacy.writer;

import net.md_5.bungee.protocol.DefinedPacket;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.pipeline.version.legacy.packets.TransformedPacket;

public interface BungeePacketTransformer {

	public TransformedPacket[] transformBungeePacket(ProtocolVersion version, DefinedPacket packet);

	public TransformedPacket[] transformTransformedPacket(ProtocolVersion version, TransformedPacket packet);

}
