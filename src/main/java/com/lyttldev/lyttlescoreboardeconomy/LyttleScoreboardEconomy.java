package com.lyttldev.lyttlescoreboardeconomy;

import com.lyttldev.lyttlescoreboardeconomy.commands.*;
import com.lyttldev.lyttlescoreboardeconomy.modules.*;
import com.lyttldev.lyttlescoreboardeconomy.utils.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicesManager;

public class LyttleScoreboardEconomy extends JavaPlugin {
    public VaultEconomy economyImplementer;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Console.init(this);
        Message.init(this);
        saveDefaultConfig();
        setupVaultEconomy();

        // Commands
        new TokensCommand(this);
        new BaltopCommand(this);
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
}