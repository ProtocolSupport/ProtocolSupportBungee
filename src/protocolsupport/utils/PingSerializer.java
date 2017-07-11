package protocolsupport.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.chat.TextComponentSerializer;
import net.md_5.bungee.chat.TranslatableComponentSerializer;

public class PingSerializer {

	private static final Gson gson = new GsonBuilder().registerTypeAdapter(ServerPing.class, new ServerPingDeserializer()).create();

	private static final Gson chatGson = new GsonBuilder()
	.registerTypeAdapter(BaseComponent.class, new ComponentSerializer())
	.registerTypeAdapter(TextComponent.class, new TextComponentSerializer())
	.registerTypeAdapter(TranslatableComponent.class, new TranslatableComponentSerializer())
	.create();

	public static String fromJSON(int protocolVersion, String json) {
		ServerPing serverPing = gson.fromJson(json, ServerPing.class);
		return
		"§1\u0000" +
		protocolVersion +
		"\u0000" +
		serverPing.version.name +
		"\u0000" +
		serverPing.description +
		"\u0000" +
		serverPing.players.online +
		"\u0000" +
		serverPing.players.max;
	}


	private static final class ServerPingDeserializer implements JsonDeserializer<ServerPing> {

		@Override
		public ServerPing deserialize(JsonElement element, Type type, JsonDeserializationContext ctx) throws JsonParseException {
			JsonObject root = element.getAsJsonObject();
			JsonObject version = root.get("version").getAsJsonObject();
			JsonObject players = root.get("players").getAsJsonObject();
			return new ServerPing(
				new ServerPingVersion(version.get("name").getAsString()),
				new ServerPingPlayers(players.get("online").getAsInt(), players.get("max").getAsInt()),
				chatGson.fromJson(root.get("description"), BaseComponent.class).toLegacyText()
			);
		}

	}

	private static final class ServerPing {
		private final ServerPingVersion version;
		private final ServerPingPlayers players;
		private final String description;
		public ServerPing(ServerPingVersion version, ServerPingPlayers players, String description) {
			this.version = version;
			this.players = players;
			this.description = description;
		}
	}

	private static final class ServerPingVersion {
		private final String name;
		public ServerPingVersion(String name) {
			this.name = name;
		}
	}

	private static final class ServerPingPlayers {
		private final int max;
		private final int online;
		public ServerPingPlayers(int online, int max) {
			this.online = online;
			this.max = max;
		}
	}

}
