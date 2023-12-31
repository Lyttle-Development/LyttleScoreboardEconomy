package com.lyttldev.lyttlescoreboardeconomy.utils;

import com.lyttldev.lyttlescoreboardeconomy.LyttleScoreboardEconomy;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class Console {
    public static LyttleScoreboardEconomy plugin;

    public static void init(LyttleScoreboardEconomy plugin) {
        Console.plugin = plugin;
    }

    public static void run(String command) {
        if (command == null || command.isEmpty()) return;
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        Bukkit.getScheduler().callSyncMethod( plugin, () -> Bukkit.dispatchCommand( console, command ) );
    }

    public static void log(String message) {
        Bukkit.getLogger().info(message);
    }
}
