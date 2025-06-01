package com.lyttldev.lyttlescoreboardeconomy.commands;

import com.lyttldev.lyttlescoreboardeconomy.LyttleScoreboardEconomy;
import com.lyttledev.lyttleutils.types.Message.Replacements;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.*;
import java.util.stream.Collectors;

public class BaltopCommand implements CommandExecutor, TabExecutor {
    private final LyttleScoreboardEconomy plugin;
    private MiniMessage mini = MiniMessage.miniMessage();

    public BaltopCommand(LyttleScoreboardEconomy plugin) {
        this.plugin = plugin;
        plugin.getCommand("baltop").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("lyttleeconomyscoreboard.baltop")) {
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
        }

        Player player = (Player) sender;
        int page = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        int pageSize = 10;
        List<Map.Entry<String, Double>> topPlayers = getTopPlayers();
        int pages = topPlayers.size() >= pageSize ? (int) Math.ceil(topPlayers.size() / pageSize) : 1;

        Replacements.Builder replacements = Replacements.builder()
                .add("<PAGE>", String.valueOf(page))
                .add("<PAGES>", String.valueOf(pages))
                .add("<PAGE_SIZE>", String.valueOf(pageSize));

        if (page < 1) {
            plugin.message.sendMessage(sender, "baltop_page_zero", replacements.build(), player);
            return true;
        }

        if (page > pages) {
            plugin.message.sendMessage(sender, "baltop_page_maxed", replacements.build(), player);
            return true;
        }

        replacements.add("<TOP_AMOUNT>", String.valueOf(10 * page));
        Component title = plugin.message.getMessage("baltop_title", replacements.build(), player);
        Component footer = plugin.message.getMessage("baltop_footer", replacements.build(), player);

        StringBuilder message = new StringBuilder(mini.serialize(title));
        for (int i = 0; i < pageSize; i++) {
            if (i >= topPlayers.size()) {
                break;
            }
            Map.Entry<String, Double> p = topPlayers.get(i + (page - 1) * pageSize);
            int nr = i + 1 + (page - 1) * pageSize;
            message.append("\n");
            replacements
                    .add("<NR>", String.valueOf(nr))
                    .add("<PLAYER>", p.getKey())
                    .add("<AMOUNT>", String.valueOf(p.getValue()))
                    .add("<NUMBER>", String.valueOf(nr));
            Component line = plugin.message.getMessage("baltop_line", replacements.build(), player);
            message.append(mini.serialize(line));
        }
        message.append("\n").append(mini.serialize(footer));
        replacements.add("<TOP_AMOUNT>", String.valueOf(10 * page));
        plugin.message.sendMessageRaw(sender, mini.deserialize(message.toString()));

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
        return List.of();
    }
}
