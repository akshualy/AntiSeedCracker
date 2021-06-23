package me.gadse.antiseedcracker;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ASCCommand implements CommandExecutor {

    private final AntiSeedCracker plugin;

    public ASCCommand(AntiSeedCracker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion());
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(plugin.getDescription().getName() + " config reloaded.");
            return true;
        }

        return false;
    }
}
