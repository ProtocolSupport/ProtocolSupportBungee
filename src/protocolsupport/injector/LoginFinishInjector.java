package protocolsupport.injector;

import java.util.HashMap;

import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;

import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginFinishInjector implements Listener {

	private final HashMap<ProtocolVersion, ILoginCallbackInjector> injectors = new HashMap<ProtocolVersion, ILoginCallbackInjector>();
	{
		protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.LoginCallbackInjector legacyInjector = new protocolsupport.protocol.transformer.v_1_4_1_5_1_6_core.LoginCallbackInjector();
		injectors.put(ProtocolVersion.MINECRAFT_1_6_4, legacyInjector);
		injectors.put(ProtocolVersion.MINECRAFT_1_6_2, legacyInjector);
		injectors.put(ProtocolVersion.MINECRAFT_1_5_2, legacyInjector);
		injectors.put(ProtocolVersion.MINECRAFT_1_4_7, legacyInjector);
	};

	@EventHandler(priority = -65)
	public void onLogin(LoginEvent event) {
		ILoginCallbackInjector injector = injectors.get(ProtocolSupportAPI.getProtocolVersion(event.getConnection().getAddress()));
		if (injector != null) {
			injector.inject(event);
		}
	}

	public static interface ILoginCallbackInjector {

		public void inject(LoginEvent event);

	}

}
