package protocolsupport;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import protocolsupport.injector.NettyInjector;
import protocolsupport.protocol.listeners.LoginFinishInjector;
import protocolsupport.protocol.listeners.ServerConnectListener;

public class ProtocolSupport extends Plugin {

	@Override
	public void onLoad() {
		try {
			NettyInjector.inject();
			getProxy().getPluginManager().registerListener(this, new LoginFinishInjector());
			getProxy().getPluginManager().registerListener(this, new ServerConnectListener());
		} catch (Throwable t) {
			t.printStackTrace();
			ProxyServer.getInstance().stop();
		}
	}

}
