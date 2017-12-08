package protocolsupport.protocol.utils;

import java.net.InetSocketAddress;

public class EncapsulatedProtocolInfo {

	private final boolean hasCompression;
	private final InetSocketAddress address;

	public EncapsulatedProtocolInfo(InetSocketAddress address, boolean hasCompression) {
		this.address = address;
		this.hasCompression = hasCompression;
	}

	public InetSocketAddress getAddress() {
		return address;
	}

	public boolean hasCompression() {
		return hasCompression;
	}

}