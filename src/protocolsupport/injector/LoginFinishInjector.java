package protocolsupport.injector;

import java.util.HashMap;

import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class LoginFinishInjector implements Listener {

	private final HashMap<ProtocolVersion, ILoginCallbackInjector> injectors = new HashMap<ProtocolVersion, ILoginCallbackInjector>();
	{
		injectors.put(ProtocolVersion.MINECRAFT_1_6_4, new protocolsupport.protocol.transformer.v_1_5_v1_6_shared.LoginCallbackInjector());
		injectors.put(ProtocolVersion.MINECRAFT_1_6_2, new protocolsupport.protocol.transformer.v_1_5_v1_6_shared.LoginCallbackInjector());
		injectors.put(ProtocolVersion.MINECRAFT_1_5_2, new protocolsupport.protocol.transformer.v_1_5_v1_6_shared.LoginCallbackInjector());
	};

	@EventHandler(priority = EventPriority.LOWEST)
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
