package com.lyttldev.lyttlescoreboardeconomy.commands;

import com.lyttldev.lyttlescoreboardeconomy.LyttleScoreboardEconomy;
import com.lyttledev.lyttleutils.types.Message.Replacements;
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
                        plugin.message.sendMessage(player, "tokens_player_not_online");
                        return true;
                    }

                    if (target == player) {
                        plugin.message.sendMessage(player, "tokens_not_yourself");
                        return true;
                    }

                    if (depositAmount < 0) {
                        plugin.message.sendMessage(player, "tokens_negative");
                        return true;
                    }

                    if (depositAmount > plugin.economyImplementer.getBalance(player)) {
                        plugin.message.sendMessage(player, "You do not have enough tokens to send.");
                        return true;
                    }

                    plugin.economyImplementer.depositPlayer(target, depositAmount);
                    plugin.economyImplementer.depositPlayer(player, depositAmount * -1);
                    Replacements playerReplacements = Replacements.builder()
                            .add("<PLAYER>", player.getName())
                            .add("<AMOUNT>", String.valueOf(depositAmount))
                            .build();
                    Replacements targetReplacements = Replacements.builder()
                            .add("<PLAYER>", target.getName())
                            .add("<AMOUNT>", String.valueOf(depositAmount))
                            .build();
                    plugin.message.sendMessage(player, "tokens_send", playerReplacements, player);
                    plugin.message.sendMessage(target, "tokens_received", targetReplacements, target);
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
                    Replacements replacements = Replacements.builder()
                        .add("<PLAYER>", target.getName())
                        .add("<AMOUNT>", String.valueOf(balance))
                        .build();
                    plugin.message.sendMessage(player, "tokens_balance", replacements);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }

        // get own tokens
        try {
            int balance = (int)  plugin.economyImplementer.getBalance(player);
            Replacements replacements = Replacements.builder()
                .add("<PLAYER>", player.getName())
                .add("<AMOUNT>", String.valueOf(balance))
                .build();
            plugin.message.sendMessage(player, "tokens_balance_self", replacements);
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
