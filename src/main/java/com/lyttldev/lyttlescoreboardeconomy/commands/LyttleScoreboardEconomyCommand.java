package com.lyttldev.lyttlescoreboardeconomy.commands;

import com.lyttldev.lyttlescoreboardeconomy.LyttleScoreboardEconomy;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;


import java.util.List;

public class LyttleScoreboardEconomyCommand implements CommandExecutor, TabCompleter {
    private final LyttleScoreboardEconomy plugin;

    public LyttleScoreboardEconomyCommand(LyttleScoreboardEconomy plugin) {
        plugin.getCommand("lyttlescoreboardeconomy").setExecutor(this);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check for permission
        if (!(sender.hasPermission("lyttlescoreboardeconomy.lyttlescoreboardeconomy"))) {
            plugin.message.sendMessage(sender, "no_permission");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("plugin version: 1.2.0");
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.config.reload();
                plugin.message.sendMessageRaw(sender, Component.text("The config has been reloaded"));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] arguments) {
        if (arguments.length == 1) {
            return List.of("reload");
        }

        return List.of();
    }
}
