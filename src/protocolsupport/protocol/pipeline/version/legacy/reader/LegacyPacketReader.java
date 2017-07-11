package protocolsupport.protocol.pipeline.version.legacy.reader;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.PacketWrapper;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.pipeline.version.legacy.packets.ChatPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.ClientSettingsPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.ClientStatusPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.EncryptionRequestPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.EncryptionResponsePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.HandshakePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.KeepAlivePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.KickPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.LoginPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.LoginRequestPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.LoginSuccessPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.PlayerListItemPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.PluginMessagePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.RespawnPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.ScoreboardDispayPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.ScoreboardObjectivePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.ScoreboardScorePacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.StatusRequestPacket;
import protocolsupport.protocol.pipeline.version.legacy.packets.TeamPacket;
import protocolsupport.utils.Utils;

public class LegacyPacketReader {

	public static PacketWrapper[] readPacket(ProtocolVersion version, int packetId, ByteBuf buf) throws IOException {
		switch (packetId) {
			case 0x00: {
				return new PacketWrapper[] { readDefinedPacket(packetId, new KeepAlivePacket(), buf) };
			}
			case 0x01: {
				return new PacketWrapper[] { new PacketWrapper(new LoginSuccessPacket(), Unpooled.EMPTY_BUFFER), readDefinedPacket(packetId, new LoginPacket(), buf) };
			}
			case 0x02: {
				HandshakePacket packet = new HandshakePacket();
				PacketWrapper handshakepacket = readDefinedPacket(packetId, packet, buf);
				return new PacketWrapper[] { handshakepacket, new PacketWrapper(new LoginRequestPacket(packet.getUsername()), Unpooled.EMPTY_BUFFER) };
			}
			case 0x03: {
				return new PacketWrapper[] { readDefinedPacket(packetId, new ChatPacket(), buf) };
			}
			case 0x09: {
				return new PacketWrapper[] { readDefinedPacket(packetId, new RespawnPacket(), buf) };
			}
			case 0xC9: {
				return new PacketWrapper[] { readDefinedPacket(packetId, new PlayerListItemPacket(), buf) };
			}
			case 0xCC: {
				return new PacketWrapper[] { readDefinedPacket(packetId, new ClientSettingsPacket(), buf) };
			}
			case 0xCE: {
				return new PacketWrapper[] { readDefinedPacket(packetId, new ScoreboardObjectivePacket(), buf) };
			}
			case 0xCF: {
				return new PacketWrapper[] { readDefinedPacket(packetId, new ScoreboardScorePacket(), buf) };
			}
			case 0xCD: {
				return new PacketWrapper[] { readDefinedPacket(packetId, new ClientStatusPacket(), buf) };
			}
			case 0xD0: {
				return new PacketWrapper[] { readDefinedPacket(packetId, new ScoreboardDispayPacket(), buf) };
			}
			case 0xD1: {
				return new PacketWrapper[] { readDefinedPacket(packetId, new TeamPacket(), buf) };
			}
			case 0xFC: {
				return new PacketWrapper[] { readDefinedPacket(packetId, new EncryptionResponsePacket(), buf) };
			}
			case 0xFA: {
				return new PacketWrapper[] { readDefinedPacket(packetId, new PluginMessagePacket(), buf) };
			}
			case 0xFD: {
				return new PacketWrapper[] { readDefinedPacket(packetId, new EncryptionRequestPacket(), buf) };
			}
			case 0xFE: {
				HandshakePacket packet = new HandshakePacket();
				packet.setRequestedProtocol(1);
				return new PacketWrapper[] { new PacketWrapper(packet, Unpooled.EMPTY_BUFFER), readDefinedPacket(packetId, new StatusRequestPacket(), buf) };
			}
			case 0xFF: {
				return new PacketWrapper[] { readDefinedPacket(packetId, new KickPacket(), buf) };
			}
			default: {
				switch (version) {
					case MINECRAFT_1_6_4:
					case MINECRAFT_1_6_2: {
						return new PacketWrapper[] { new PacketWrapper(null, protocolsupport.protocol.pipeline.version.legacy.reader.V6PacketReader.readPacket(packetId, buf)) };
					}
					case MINECRAFT_1_5_2: {
						return new PacketWrapper[] { new PacketWrapper(null, protocolsupport.protocol.pipeline.version.legacy.reader.V5PacketReader.readPacket(packetId, buf)) };
					}
					case MINECRAFT_1_4_7: {
						return new PacketWrapper[] { new PacketWrapper(null, protocolsupport.protocol.pipeline.version.legacy.reader.V4PacketReader.readPacket(packetId, buf)) };
					}
					default: {
						throw new IOException("Wrong protocol version");
					}
				}
			}
		}
	}

	private static PacketWrapper readDefinedPacket(int packetId, DefinedPacket packet, ByteBuf buf) {
		int readerIndex = buf.readerIndex();
		packet.read(buf);
		int bytesread = buf.readerIndex() - readerIndex;
		buf.readerIndex(readerIndex);
		ByteBuf rawpacketdata = Unpooled.buffer();
		rawpacketdata.writeByte(packetId);
		Utils.rewriteBytes(buf, rawpacketdata, bytesread);
		return new PacketWrapper(packet, rawpacketdata);
	}

}
