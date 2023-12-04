package com.lyttldev.mapleeconomy.utils;

import com.lyttldev.mapleeconomy.MapleEconomy;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class Console {
    public static MapleEconomy plugin;

    public static void init(MapleEconomy plugin) {
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
