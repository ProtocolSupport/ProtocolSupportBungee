package protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.reader;

import java.io.IOException;

import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.ChatPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.ClientSettingsPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.ClientStatusPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.EncryptionRequestPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.EncryptionResponsePacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.HandshakePacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.KeepAlivePacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.KickPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.LoginPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.LoginRequestPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.PlayerListItemPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.PluginMessagePacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.RespawnPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.ScoreboardDispayPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.ScoreboardObjectivePacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.ScoreboardScorePacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.StatusRequestPacket;
import protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.packets.TeamPacket;
import protocolsupport.utils.Utils;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.PacketWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class PacketReader {

	public static PacketWrapper[] readPacket(ProtocolVersion version, int packetId, ByteBuf buf) throws IOException {
		switch (packetId) {
			case 0x00: {
				return new PacketWrapper[] { readDefinedPacket(packetId, new KeepAlivePacket(), buf) };
			}
			case 0x01: {
				return new PacketWrapper[] { readDefinedPacket(packetId, new LoginPacket(), buf) };
			}
			case 0x02: {
				HandshakePacket packet = new HandshakePacket();
				PacketWrapper handshakepacket = readDefinedPacket(packetId, packet, buf);
				return new PacketWrapper[] { handshakepacket, new PacketWrapper(new LoginRequestPacket(packet.getUsername()), Unpooled.buffer()) };
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
				return new PacketWrapper[] { new PacketWrapper(packet, Unpooled.buffer()), readDefinedPacket(packetId, new StatusRequestPacket(), buf) };
			}
			case 0xFF: {
				return new PacketWrapper[] { readDefinedPacket(packetId, new KickPacket(), buf) };
			}
			default: {
				switch (version) {
					case MINECRAFT_1_6_4:
					case MINECRAFT_1_6_2: {
						return new PacketWrapper[] { new PacketWrapper(null, protocolsupport.protocol.transformer.v_1_6.reader.NonDefinedPacketReader.readPacket(packetId, buf)) };
					}
					case MINECRAFT_1_5_2: {
						return new PacketWrapper[] { new PacketWrapper(null, protocolsupport.protocol.transformer.v_1_5.reader.NonDefinedPacketReader.readPacket(packetId, buf)) };
					}
					case MINECRAFT_1_4_7: {
						return new PacketWrapper[] { new PacketWrapper(null, protocolsupport.protocol.transformer.v_1_4.reader.NonDefinedPacketReader.readPacket(packetId, buf)) };
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
		int length = buf.readerIndex() - readerIndex;
		buf.readerIndex(readerIndex);
		ByteBuf data = Unpooled.buffer();
		data.writeByte(packetId);
		Utils.rewriteBytes(buf, data, length);
		return new PacketWrapper(packet, data);
	}

}
