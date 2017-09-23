package protocolsupport;

import java.util.List;
import java.util.stream.Collectors;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;

public class CommandHandler extends Command {

	public CommandHandler() {
		super("protocolsupportbungee", "protocolsupport.cmds", "psb");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if ((args.length == 1) || (args.length == 2)) {
			if (args[0].equalsIgnoreCase("list")) {
				sender.sendMessage(new TextComponent(ChatColor.GREEN + "ProtocolSupport Players:"));
				for (ProtocolVersion version : ProtocolVersion.getAllSupported()) {
					List<String> players = ProxyServer.getInstance().getPlayers().stream()
					.filter(player -> ProtocolSupportAPI.getProtocolVersion(player) == version)
					.map(player -> player.getName())
					.collect(Collectors.toList());
					if (!players.isEmpty() || ((args.length == 2) && (args[1].equalsIgnoreCase("v") || args[1].equalsIgnoreCase("verbose")))) {
						sender.sendMessage(new TextComponent(ChatColor.GOLD + "[" + version.getName() + "]: " + ChatColor.GREEN + String.join(", ", players)));
					}
				}
				if ((args.length == 1) || !(args[1].equalsIgnoreCase("v") || args[1].equalsIgnoreCase("verbose"))) {
					sender.sendMessage(new TextComponent(ChatColor.GOLD + "List all compatible versions using " + ChatColor.GREEN + "/psb list verbose"));
				}
			} else if (args[0].equalsIgnoreCase("connections")) {
				for (Connection connection : ProtocolSupportAPI.getConnections()) {
					sender.sendMessage(new TextComponent(ChatColor.GREEN + connection.toString()));
				}
			}
		}
	}

}
