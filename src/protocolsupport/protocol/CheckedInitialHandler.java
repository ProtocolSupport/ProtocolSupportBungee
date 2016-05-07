package protocolsupport.protocol;

import protocolsupport.utils.FakeChannelContext;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.netty.ChannelWrapper;

public class CheckedInitialHandler extends InitialHandler {

	public CheckedInitialHandler(BungeeCord bungee, ListenerInfo listener) {
		super(bungee, listener);
	}

	@Override
    public void connected(final ChannelWrapper channel) throws Exception {
    	super.connected(new CheckedChannelWrapper(new FakeChannelContext(channel.getHandle())));
    }

}
