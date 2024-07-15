package com.lyttldev.lyttlescoreboardeconomy.types;

import com.lyttldev.lyttlescoreboardeconomy.LyttleScoreboardEconomy;

public class Configs {
    private final LyttleScoreboardEconomy plugin;

    // Configs
    public Config general;
    public Config messages;

    // Default configs
    public Config defaultMessages;
    public Config defaultGeneral;

    public Configs(LyttleScoreboardEconomy plugin) {
        this.plugin = plugin;

        // Configs
        general = new Config(plugin, "config.yml");
        messages = new Config(plugin, "messages.yml");

        // Default configs
        defaultGeneral = new Config(plugin, "#defaults/config.yml");
        defaultMessages = new Config(plugin, "#defaults/messages.yml");
    }

    public void reload() {
        general.reload();
        messages.reload();

        plugin.reloadConfig();
    }

    private String getConfigPath(String path) {
        return plugin.getConfig().getString("configs." + path);
    }
}
