package com.lyttldev.lyttlescoreboardeconomy;

import com.lyttldev.lyttlescoreboardeconomy.commands.*;
import com.lyttldev.lyttlescoreboardeconomy.modules.*;
import com.lyttldev.lyttlescoreboardeconomy.types.Configs;

import com.lyttledev.lyttleutils.utils.communication.Console;
import com.lyttledev.lyttleutils.utils.communication.Message;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicesManager;

import java.io.File;

public class LyttleScoreboardEconomy extends JavaPlugin {
    public VaultEconomy economyImplementer;
    public Configs config;
    public Console console;
    public Message message;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        // Setup config after creating the configs
        config = new Configs(this);
        // Migrate config
        migrateConfig();

        // Plugin startup logic
        this.console = new Console(this);
        this.message = new Message(this, config.messages);
        saveDefaultConfig();
        setupVaultEconomy();

        // Commands
        new TokensCommand(this);
        new BaltopCommand(this);
        new LyttleScoreboardEconomyCommand(this);
    }

    private void setupVaultEconomy() {
        try {
            ServicesManager servicesManager = Bukkit.getServicesManager();
            RegisteredServiceProvider<VaultEconomy> rsp = servicesManager.getRegistration(VaultEconomy.class);

            if (rsp == null) {
                economyImplementer = new VaultEconomy(this);
                servicesManager.register(Economy.class, economyImplementer, this, ServicePriority.Normal);
                return;
            }
            economyImplementer = rsp.getProvider();
        } catch (Exception e) {
            getLogger().severe("Failed to set up CustomEconomy! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void saveDefaultConfig() {
        String configPath = "config.yml";
        if (!new File(getDataFolder(), configPath).exists())
            saveResource(configPath, false);

        String messagesPath = "messages.yml";
        if (!new File(getDataFolder(), messagesPath).exists())
            saveResource(messagesPath, false);

        // Defaults:
        String defaultPath = "#defaults/";
        String defaultGeneralPath =  defaultPath + configPath;
        saveResource(defaultGeneralPath, true);

        String defaultMessagesPath =  defaultPath + messagesPath;
        saveResource(defaultMessagesPath, true);
    }

    private void migrateConfig() {
        if (!config.general.contains("config_version")) {
            config.general.set("config_version", 0);
        }

        switch (config.general.get("config_version").toString()) {
            case "0":
                // Migrate config entries.
                config.general.remove("prefix");
                config.messages.set("prefix", config.defaultMessages.get("prefix"));

                // Update config version.
                config.general.set("config_version", 1);

                // Recheck if the config is fully migrated.
                migrateConfig();
                break;
            case "1":
                // Migrate config entries.
                config.general.set("scoreboard_objective", config.defaultGeneral.get("scoreboard_objective"));
                config.general.set("scoreboard_objective_name", config.defaultGeneral.get("scoreboard_objective_name"));

                // Update config version.
                config.general.set("config_version", 2);

                // Recheck if the config is fully migrated.
                migrateConfig();
                break;
            default:
                break;
        }
    }
}