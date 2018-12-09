package protocolsupport.protocol.pipeline.version.v_pe;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.packet.BossBar;
import net.md_5.bungee.protocol.packet.Chat;
import net.md_5.bungee.protocol.packet.EncryptionRequest;
import net.md_5.bungee.protocol.packet.KeepAlive;
import net.md_5.bungee.protocol.packet.Kick;
import net.md_5.bungee.protocol.packet.Login;
import net.md_5.bungee.protocol.packet.LoginSuccess;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import net.md_5.bungee.protocol.packet.PluginMessage;
import net.md_5.bungee.protocol.packet.Respawn;
import net.md_5.bungee.protocol.packet.ScoreboardDisplay;
import net.md_5.bungee.protocol.packet.ScoreboardObjective;
import net.md_5.bungee.protocol.packet.ScoreboardScore;
import net.md_5.bungee.protocol.packet.StatusResponse;
import net.md_5.bungee.protocol.packet.TabCompleteResponse;
import net.md_5.bungee.protocol.packet.Team;
import net.md_5.bungee.protocol.packet.Title;
import protocolsupport.api.Connection;
import protocolsupport.protocol.packet.middleimpl.writeable.NoopWriteablePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.login.v_pe.LoginSuccessPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe.KickPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe.RespawnPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe.StartGamePacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe.ToClientChatPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe.CustomEventPacket;
import protocolsupport.protocol.packet.middleimpl.writeable.status.v_pe.StatusResponsePacket;
import protocolsupport.protocol.pipeline.version.AbstractPacketEncoder;
import protocolsupport.protocol.storage.NetworkDataCache;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

public class ToClientPacketEncoder extends AbstractPacketEncoder {

	{
		registry.register(EncryptionRequest.class, NoopWriteablePacket.class);
		registry.register(LoginSuccess.class, LoginSuccessPacket.class);
		registry.register(Login.class, StartGamePacket.class);
		registry.register(StatusResponse.class, StatusResponsePacket.class);
		registry.register(Kick.class, KickPacket.class);
		registry.register(KeepAlive.class, NoopWriteablePacket.class);
		registry.register(Respawn.class, RespawnPacket.class);
		registry.register(Chat.class, ToClientChatPacket.class);
		registry.register(ScoreboardDisplay.class, NoopWriteablePacket.class);
		registry.register(ScoreboardObjective.class, NoopWriteablePacket.class);
		registry.register(ScoreboardScore.class, NoopWriteablePacket.class);
		registry.register(Team.class, NoopWriteablePacket.class);
		registry.register(PlayerListItem.class, NoopWriteablePacket.class); //TODO: implement it
		registry.register(TabCompleteResponse.class, NoopWriteablePacket.class);
		registry.register(BossBar.class, NoopWriteablePacket.class);
		registry.register(Title.class, NoopWriteablePacket.class);
		registry.register(PluginMessage.class, CustomEventPacket.class);
	}

	public ToClientPacketEncoder(Connection connection, NetworkDataCache cache) {
		super(connection, cache);
	}

	protected ArrayList<Pair<Object, ChannelPromise>> packetCache = new ArrayList<>();

	@Override
	public void write(final ChannelHandlerContext ctx, final Object msgObject, final ChannelPromise promise) throws Exception {

		if (acceptOutboundMessage(msgObject)) {
			DefinedPacket msg = (DefinedPacket) msgObject;
			if (msg instanceof PluginMessage && cache.isStashingClientPackets() && ((PluginMessage)msg).getTag().equals("ps:bungeeunlock")) {
				cache.setStashingClientPackets(false);
				//copy list so we can safely recurse back into this method
				ArrayList<Pair<Object, ChannelPromise>> packetCacheCopy = packetCache;
				packetCache = new ArrayList<>();
				for (Map.Entry<Object, ChannelPromise> cachedPacket : packetCacheCopy) {
					write(ctx, cachedPacket.getKey(), cachedPacket.getValue());
				}
				return;
			}
			// check if this is the bungee initiated chunk-cache-clearing dim switch
			if (msg instanceof Respawn && ((Respawn)msg).getDimension() != cache.getRealDimension() && !cache.isStashingClientPackets()) {
				cache.setStashingClientPackets(true);
				super.write(ctx, msgObject, promise);
				return;
			}
		}
		if (cache.isStashingClientPackets()) {
			packetCache.add(new ImmutablePair(msgObject, promise));
		} else {
			super.write(ctx, msgObject, promise);
		}
	}

}
