package com.lyttldev.lyttlescoreboardeconomy.commands;

import com.lyttldev.lyttlescoreboardeconomy.LyttleScoreboardEconomy;
import com.lyttledev.lyttleutils.utils.communication.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TokensCommand implements CommandExecutor, TabExecutor {
    private final LyttleScoreboardEconomy plugin;

    public TokensCommand(LyttleScoreboardEconomy plugin) {
        plugin.getCommand("tokens").setExecutor(this);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length > 0 && args[0].equalsIgnoreCase("send")) {
            if (args.length == 3) {
                try {
                    Player target = Bukkit.getPlayer(args[1]);
                    int depositAmount = Integer.parseInt(args[2]);

                    if (target == null) {
                        Message.sendMessage(player, "That player is not online.");
                        return true;
                    }

                    if (target == player) {
                        Message.sendMessage(player, "You cannot send tokens to yourself.");
                        return true;
                    }

                    if (depositAmount < 0) {
                        Message.sendMessage(player, "You cannot send a negative amount of tokens.");
                        return true;
                    }

                    if (depositAmount > plugin.economyImplementer.getBalance(player)) {
                        Message.sendMessage(player, "You do not have enough tokens to send.");
                        return true;
                    }

                    plugin.economyImplementer.depositPlayer(target, depositAmount);
                    plugin.economyImplementer.depositPlayer(player, depositAmount * -1);
                    Message.sendMessage(player, "You have deposited &a" + depositAmount + " tokens&7 into &e" + target.getName() + "'s&7 account");
                    Message.sendMessage(target, "&e" + player.getName() + "&7 has deposited &a" + depositAmount + " tokens&7 into your account");
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
            return false;
        }

        // Get user tokens
        if (args.length > 0 && args[0].equalsIgnoreCase("balance")) {
            if (args.length == 2) {
                try {
                    Player target = Bukkit.getPlayer(args[1]);
                    int balance = (int)  plugin.economyImplementer.getBalance(target);
                    Message.sendMessage(player, "&8" + target.getName() + "&7 has &a" + balance + " tokens&7.");
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }

        // get own tokens
        try {
            int balance = (int)  plugin.economyImplementer.getBalance(player);
            Message.sendMessage(player, "You have &a" + balance + " tokens&7.");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] arguments) {
        if (arguments.length == 1) {
            List<String> options = Arrays.asList("send", "balance");
            List<String> result = new ArrayList<>(Collections.emptyList());
            for (String option : options) {
                if (option.toLowerCase().startsWith(arguments[0].toLowerCase())) {
                    result.add(option);
                }
            }
            return result;
        }

        if (arguments.length == 2) {
            return null;
        }

        return List.of();
    }
}
