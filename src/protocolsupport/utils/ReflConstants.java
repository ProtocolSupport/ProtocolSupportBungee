package protocolsupport.utils;

import net.md_5.bungee.connection.InitialHandler;

public class ReflConstants {

	private static Object initialHandlerFinishedState;
	public static Object getInitialHandlerFinishedState() throws ClassNotFoundException {
		if (initialHandlerFinishedState == null) {
			initialHandlerFinishedState = Class.forName(InitialHandler.class.getName()+"$State").getEnumConstants()[5];
		}
		return initialHandlerFinishedState;
	}

}
