package com.lyttldev.lyttlescoreboardeconomy;

import com.lyttldev.lyttlescoreboardeconomy.commands.*;
import com.lyttldev.lyttlescoreboardeconomy.modules.*;
import com.lyttldev.lyttlescoreboardeconomy.types.Configs;
import com.lyttldev.lyttlescoreboardeconomy.utils.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicesManager;

public class LyttleScoreboardEconomy extends JavaPlugin {
    public VaultEconomy economyImplementer;
    public Configs config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        // Setup config after creating the configs
        config = new Configs(this);
        // Migrate config
        migrateConfig();

        // Plugin startup logic
        Console.init(this);
        Message.init(this);
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
                economyImplementer = new VaultEconomy();
                servicesManager.register(Economy.class, economyImplementer, this, ServicePriority.Normal);
                return;
            }
            economyImplementer = rsp.getProvider();
        } catch (Exception e) {
            getLogger().severe("Failed to set up CustomEconomy! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
        }
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
            default:
                break;
        }
    }
}