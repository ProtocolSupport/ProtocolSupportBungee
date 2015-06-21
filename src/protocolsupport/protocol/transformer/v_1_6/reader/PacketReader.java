package protocolsupport.protocol.transformer.v_1_6.reader;

import protocolsupport.protocol.transformer.v_1_6.packets.ChatPacket;
import protocolsupport.protocol.transformer.v_1_6.packets.ClientSettingsPacket;
import protocolsupport.protocol.transformer.v_1_6.packets.ClientStatusPacket;
import protocolsupport.protocol.transformer.v_1_6.packets.EncryptionRequestPacket;
import protocolsupport.protocol.transformer.v_1_6.packets.EncryptionResponsePacket;
import protocolsupport.protocol.transformer.v_1_6.packets.HandshakePacket;
import protocolsupport.protocol.transformer.v_1_6.packets.KeepAlivePacket;
import protocolsupport.protocol.transformer.v_1_6.packets.KickPacket;
import protocolsupport.protocol.transformer.v_1_6.packets.LoginPacket;
import protocolsupport.protocol.transformer.v_1_6.packets.LoginRequestPacket;
import protocolsupport.protocol.transformer.v_1_6.packets.PlayerListItemPacket;
import protocolsupport.protocol.transformer.v_1_6.packets.PluginMessagePacket;
import protocolsupport.protocol.transformer.v_1_6.packets.RespawnPacket;
import protocolsupport.protocol.transformer.v_1_6.packets.ScoreboardDispayPacket;
import protocolsupport.protocol.transformer.v_1_6.packets.ScoreboardObjectivePacket;
import protocolsupport.protocol.transformer.v_1_6.packets.ScoreboardScorePacket;
import protocolsupport.protocol.transformer.v_1_6.packets.StatusRequestPacket;
import protocolsupport.protocol.transformer.v_1_6.packets.TeamPacket;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.PacketWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class PacketReader {

	public static PacketWrapper[] readPacket(int packetId, ByteBuf buf) {
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
				return new PacketWrapper[] { new PacketWrapper(null, NonDefinedPacketReader.readPacket(packetId, buf)) };
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
		data.writeBytes(buf.readBytes(length).array());
		return new PacketWrapper(packet, data.readBytes(data.readableBytes()));
	}

}
