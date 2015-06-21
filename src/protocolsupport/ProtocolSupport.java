package protocolsupport;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import protocolsupport.injector.NettyInjector;

public class ProtocolSupport extends Plugin {

	@Override
	public void onLoad() {
		try {
			NettyInjector.inject();
			getProxy().getPluginManager().registerListener(this, new protocolsupport.protocol.transformer.v_1_5.LoginListener());
			getProxy().getPluginManager().registerListener(this, new protocolsupport.protocol.transformer.v_1_6.LoginListener());
		} catch (Throwable t) {
			t.printStackTrace();
			ProxyServer.getInstance().stop();
		}
	}

}
