package com.lyttldev.lyttlescoreboardeconomy.commands;

import com.lyttldev.lyttlescoreboardeconomy.LyttleScoreboardEconomy;
import com.lyttldev.lyttlescoreboardeconomy.utils.Message;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.*;
import java.util.stream.Collectors;

public class BaltopCommand implements CommandExecutor, TabExecutor {
    private final LyttleScoreboardEconomy plugin;

    public BaltopCommand(LyttleScoreboardEconomy plugin) {
        plugin.getCommand("baltop").setExecutor(this);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            int page = args.length > 0 ? Integer.parseInt(args[0]) : 1;
            int pageSize = 10;
            List<Map.Entry<String, Double>> topPlayers = getTopPlayers();
            int pages = topPlayers.size() >= pageSize ? (int) Math.ceil(topPlayers.size() / pageSize) : 1;
            if (page < 1) {
                Message.sendPlayer((Player) sender, "Page must be greater than 0.", true);
                return true;
            }

            if (page > pages) {
                Message.sendPlayer((Player) sender, "There are only " + pages + " pages of top players.", true);
                return true;
            }

            String message = "Top " + 10 * page + " players by tokens:";
            for (int i = 0; i < pageSize; i++) {
                if (i >= topPlayers.size()) {
                    break;
                }
                Map.Entry<String, Double> player = topPlayers.get(i + (page - 1) * pageSize);
                int nr = i + 1 + (page - 1) * pageSize;
                message += "\n&7" + nr + ". &e" + player.getKey() + "&8: &a" + player.getValue() + " Tokens&7";
            }
            message += "\nPage " + page + "/" + pages;
            Message.sendPlayer((Player) sender, message, true);
        } else {
            sender.sendMessage("This command can only be run by a player.");
        }
        return true;
    }

    private List<Map.Entry<String, Double>> getTopPlayers() {
        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("tokens");
        if (objective == null) {
            return Collections.emptyList();
        }

        Map<String, Double> playerBalances = new HashMap<>();
        for (String entry : objective.getScoreboard().getEntries()) {
            Score score = objective.getScore(entry);
            playerBalances.put(entry, (double) score.getScore());
        }

        return playerBalances.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] arguments) {
        return Arrays.asList();
    }
}
