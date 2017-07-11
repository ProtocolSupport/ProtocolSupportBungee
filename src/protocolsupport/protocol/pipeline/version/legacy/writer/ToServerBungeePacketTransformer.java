package protocolsupport.protocol.pipeline.version.legacy.writer;

import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.packet.Chat;
import net.md_5.bungee.protocol.packet.Handshake;
import net.md_5.bungee.protocol.packet.KeepAlive;
import net.md_5.bungee.protocol.packet.PluginMessage;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.pipeline.version.legacy.packets.ChatPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.HandshakePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.KeepAlivePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.LoginRequestPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.PluginMessagePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.TransformedPacket;

public class ToServerBungeePacketTransformer implements BungeePacketTransformer {

	private static final TransformedPacket[] EMPTY = new TransformedPacket[0];

	private Handshake cachedHandshake;

	public TransformedPacket[] transformBungeePacket(ProtocolVersion version, DefinedPacket packet) {
		if (packet instanceof KeepAlive) {
			return new TransformedPacket[] { new KeepAlivePacket(((KeepAlive) packet).getRandomId()) };
		} else if (packet instanceof PluginMessage) {
			PluginMessage pmessage = (PluginMessage) packet;
			return new TransformedPacket[] { new PluginMessagePacket(pmessage.getTag(), pmessage.getData().clone(), pmessage.isAllowExtendedPacket()) };
		} else if (packet instanceof Chat) {
			return new TransformedPacket[] { new ChatPacket(((Chat) packet).getMessage()) };
		} else if (packet instanceof Handshake) {
			cachedHandshake = (Handshake) packet;
			return EMPTY;
		} else {
			throw new IllegalArgumentException("Unable to transform bungee packet " + packet.getClass().getName());
		}
	}

	public TransformedPacket[] transformTransformedPacket(ProtocolVersion version, TransformedPacket packet) {
		if (packet instanceof LoginRequestPacket) {
			return new TransformedPacket[] { new HandshakePacket(version.getId(), ((LoginRequestPacket) packet).getData(), cachedHandshake.getHost(), cachedHandshake.getPort()) };
		}
		return new TransformedPacket[] { packet };
	}

}
