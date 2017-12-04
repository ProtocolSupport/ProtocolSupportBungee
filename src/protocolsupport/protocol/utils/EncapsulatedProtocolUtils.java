package protocolsupport.protocol.utils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import protocolsupport.api.ProtocolType;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;

public class EncapsulatedProtocolUtils {

	public static EncapsulatedProtocolInfo readInfo(ByteBuf from) {
		int encapVersion = VarNumberSerializer.readVarInt(from);
		switch (encapVersion) {
			case 0x00: {
				try {
					InetSocketAddress remoteaddress = null;
					if (from.readBoolean()) {
						InetAddress address = InetAddress.getByAddress(MiscSerializer.readBytes(from, VarNumberSerializer.readVarInt(from)));
						int port = VarNumberSerializer.readVarInt(from);
						remoteaddress = new InetSocketAddress(address, port);
					}
					boolean hasCompression = from.readBoolean();
					int protocoltype = VarNumberSerializer.readVarInt(from);
					int protocolversion = VarNumberSerializer.readVarInt(from);
					return new EncapsulatedProtocolInfo(remoteaddress, hasCompression, getVersion(protocoltype, protocolversion));
				} catch (UnknownHostException e) {
					throw new DecoderException("Invalid ip address");
				}
			}
			default: {
				throw new DecoderException("Unknown encapsulated protocol version " + encapVersion);
			}
		}
	}

	public static void writeInfo(ByteBuf to, EncapsulatedProtocolInfo info) {
		VarNumberSerializer.writeVarInt(to, 0x00);
		if (info.getAddress() != null) {
			to.writeBoolean(true);
			byte[] addr = info.getAddress().getAddress().getAddress();
			VarNumberSerializer.writeVarInt(to, addr.length);
			to.writeBytes(addr);
			VarNumberSerializer.writeVarInt(to, info.getAddress().getPort());
		} else {
			to.writeBoolean(false);
		}
		to.writeBoolean(info.hasCompression());
		VarNumberSerializer.writeVarInt(to, getVersionType(info.getVersion()));
		VarNumberSerializer.writeVarInt(to, info.getVersion().getId());
	}

	public static ByteBuf createHandshake(InetSocketAddress remote, boolean hasCompression, ProtocolVersion version) {
		ByteBuf data = Unpooled.buffer();
		data.writeByte(0);
		EncapsulatedProtocolUtils.writeInfo(data, new EncapsulatedProtocolInfo(remote, hasCompression, version));
		return data;
	}

	private static ProtocolVersion getVersion(int protocoltype, int protocolversion) {
		switch (protocoltype) {
			case 0: {
				return ProtocolVersionsHelper.getOldProtocolVersion(protocoltype);
			}
			case 1: {
				return ProtocolVersionsHelper.getNewProtocolVersion(protocoltype);
			}
			case 2: {
				return ProtocolVersion.MINECRAFT_PE;
			}
			default: {
				throw new IllegalArgumentException("Unknown protocol type: " + protocoltype);
			}
		}
	}

	private static int getVersionType(ProtocolVersion version) {
		if (version.getProtocolType() == ProtocolType.PC) {
			if (version.isBeforeOrEq(ProtocolVersion.MINECRAFT_1_6_4)) {
				return 0;
			} else if (version.isAfterOrEq(ProtocolVersion.MINECRAFT_1_7_5)) {
				return 1;
			}
		} else if (version.getProtocolType() == ProtocolType.PE) {
			return 2;
		}
		throw new IllegalArgumentException("Can't convert version " + version + " to protocol type");
	}

}
