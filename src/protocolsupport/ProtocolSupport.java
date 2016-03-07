package protocolsupport;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import protocolsupport.injector.LoginFinishInjector;
import protocolsupport.injector.NettyInjector;
import protocolsupport.injector.ServerConnectInjector;

public class ProtocolSupport extends Plugin {

	@Override
	public void onLoad() {
		try {
			LoggerUtil.init(getLogger());
			getProxy().getPluginManager().registerCommand(this, new CommandHandler());
			NettyInjector.inject();
		} catch (Throwable t) {
			t.printStackTrace();
			ProxyServer.getInstance().stop();
		}
	}

	@Override
	public void onEnable() {
		getProxy().getPluginManager().registerListener(this, new LoginFinishInjector());
		getProxy().getPluginManager().registerListener(this, new ServerConnectInjector());
	}

}
