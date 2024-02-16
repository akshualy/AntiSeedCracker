package me.gadse.antiseedcracker.commands;

import me.gadse.antiseedcracker.AntiSeedCracker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AntiSeedCrackerCommand implements CommandExecutor, TabCompleter {

    private final AntiSeedCracker plugin;

    public AntiSeedCrackerCommand(AntiSeedCracker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            return false;
        }

        plugin.reloadConfig();
        plugin.reload(false);
        sender.sendMessage("AntiSeedCracker config reloaded.");
        return true;
    }

    private final Set<String> args_zero = Set.of("reload");
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && sender.hasPermission("antiseedcracker.admin")) {
            return StringUtil.copyPartialMatches(args[0], args_zero, new ArrayList<>());
        }
        return null;
    }
}
