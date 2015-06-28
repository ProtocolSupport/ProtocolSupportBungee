package protocolsupport.api;

import java.net.SocketAddress;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import protocolsupport.protocol.storage.ProtocolStorage;

public class ProtocolSupportAPI {

	public static ProtocolVersion getProtocolVersion(ProxiedPlayer player) {
		return getProtocolVersion(player.getAddress());
	}

	public static ProtocolVersion getProtocolVersion(SocketAddress address) {
		return ProtocolStorage.getProtocolVersion(address);
	}

}
