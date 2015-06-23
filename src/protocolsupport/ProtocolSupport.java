package protocolsupport;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import protocolsupport.injector.NettyInjector;
import protocolsupport.protocol.pipeline.initial.LoginFinishInjector;

public class ProtocolSupport extends Plugin {

	@Override
	public void onLoad() {
		try {
			NettyInjector.inject();
			getProxy().getPluginManager().registerListener(this, new LoginFinishInjector());
		} catch (Throwable t) {
			t.printStackTrace();
			ProxyServer.getInstance().stop();
		}
	}

}
