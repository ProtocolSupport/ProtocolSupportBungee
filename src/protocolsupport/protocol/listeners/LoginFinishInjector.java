package protocolsupport.protocol.listeners;

import java.util.HashMap;

import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class LoginFinishInjector implements Listener {

	@SuppressWarnings("serial")
	private final HashMap<ProtocolVersion, ILoginCallbackInjector> injectors = new HashMap<ProtocolVersion, ILoginCallbackInjector>() {{
		put(ProtocolVersion.MINECRAFT_1_9, new ILoginCallbackInjector() {
			@Override
			public void inject(LoginEvent event) {
			}
		});
		put(ProtocolVersion.MINECRAFT_1_8, new ILoginCallbackInjector() {
			@Override
			public void inject(LoginEvent event) {
			}
		});
		put(ProtocolVersion.MINECRAFT_1_7_10, new protocolsupport.protocol.transformer.v_1_7.LoginCallbackInjector());
		put(ProtocolVersion.MINECRAFT_1_7_5, new protocolsupport.protocol.transformer.v_1_7.LoginCallbackInjector());
		put(ProtocolVersion.MINECRAFT_1_6_4, new protocolsupport.protocol.transformer.v_1_5_v1_6_shared.LoginCallbackInjector());
		put(ProtocolVersion.MINECRAFT_1_6_2, new protocolsupport.protocol.transformer.v_1_5_v1_6_shared.LoginCallbackInjector());
		put(ProtocolVersion.MINECRAFT_1_6_1, new protocolsupport.protocol.transformer.v_1_5_v1_6_shared.LoginCallbackInjector());
		put(ProtocolVersion.MINECRAFT_1_5_2, new protocolsupport.protocol.transformer.v_1_5_v1_6_shared.LoginCallbackInjector());
		put(ProtocolVersion.MINECRAFT_1_5_1, new protocolsupport.protocol.transformer.v_1_5_v1_6_shared.LoginCallbackInjector());
		put(ProtocolVersion.MINECRAFT_1_4_7, new protocolsupport.protocol.transformer.v_1_5_v1_6_shared.LoginCallbackInjector());
	}};

	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(LoginEvent event) {
		injectors.get(ProtocolSupportAPI.getProtocolVersion(event.getConnection().getAddress())).inject(event);
	}

	public static interface ILoginCallbackInjector {

		public void inject(LoginEvent event);

	}

}
