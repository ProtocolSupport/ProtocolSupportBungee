package protocolsupport;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class CommandHandler extends Command {

	public CommandHandler() {
		super("protocolsupportbungee", "protocolsupport.cmds", "psb");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if ((args.length == 1) && args[0].equals("debug")) {
			if (LoggerUtil.isEnabled()) {
				sender.sendMessage(new TextComponent("Disabled logger"));
				LoggerUtil.setEnabled(false);
			} else {
				LoggerUtil.setEnabled(true);
				sender.sendMessage(new TextComponent("Enabled logger"));
			}
		}
	}

}
