package protocolsupport.utils;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.chat.BaseComponent;

public class Utils {

	public static String clampString(String string, int limit) {
		return string.substring(0, string.length() > limit ? limit : string.length());
	}

	public static void rewriteBytes(ByteBuf from, ByteBuf to, int length) {
		to.writeBytes(from.readBytes(length).array());
	}

	public static String toLegacyText(BaseComponent[] components) {
		if (components == null) {
			return "";
		}
		final StringBuilder out = new StringBuilder();
		for (BaseComponent component : components) {
			out.append(component.toLegacyText());
		}
		return out.toString();
	}

}
