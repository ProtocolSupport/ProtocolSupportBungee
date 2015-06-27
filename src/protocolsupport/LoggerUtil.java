package protocolsupport;

import java.util.logging.Logger;

public class LoggerUtil {

	private static boolean enabled = false;

	public static boolean isEnabled() {
		return enabled;
	}

	public static void setEnabled(boolean enabled) {
		LoggerUtil.enabled = enabled;
	}

	private static Logger log;

	static void init(Logger log) {
		LoggerUtil.log = log;
	}

	public static void debug(String message) {
		log.info(message);
	}

}
