package protocolsupport;

import java.util.logging.Level;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import protocolsupport.injector.BungeeNettyChannelInjector;

public class ProtocolSupport extends Plugin {

	@Override
	public void onLoad() {
		try {
			getProxy().getPluginManager().registerCommand(this, new CommandHandler());
			BungeeNettyChannelInjector.inject();
		} catch (Throwable t) {
			getLogger().log(Level.SEVERE, "Error occured while initalizing", t);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		if (!ProxyServer.getInstance().getConfig().isDisableEntityMetadataRewrite()) {
			getLogger().log(Level.SEVERE, "Entity metadata rewrite must be disabled in order for plugin to work");
		}
	}

}
