package protocolsupport.api;

import java.net.SocketAddress;

import protocolsupport.protocol.storage.ProtocolStorage;

public class ProtocolSupportAPI {

	public static ProtocolVersion getProtocolVersion(SocketAddress address) {
		return ProtocolStorage.getProtocolVersion(address);
	}

}
