package com.lyttldev.lyttlescoreboardeconomy.modules;

import com.lyttldev.lyttlescoreboardeconomy.LyttleScoreboardEconomy;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.*;

import java.util.List;
import java.util.Objects;

public class VaultEconomy implements Economy {
    private final LyttleScoreboardEconomy plugin;

    private final String objectiveName;
    private final String objectiveDisplayName;
    private final Scoreboard scoreboard;

    public VaultEconomy(LyttleScoreboardEconomy plugin) {
        this.plugin = plugin;
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        this.objectiveDisplayName = (String) plugin.config.general.get("scoreboard_objective");
        this.objectiveName = this.objectiveDisplayName;

        registerObjective();
    }

    private void registerObjective() {
        Objective objective = scoreboard.getObjective(objectiveName);
        if (objective == null) {
            objective = scoreboard.registerNewObjective(objectiveName, "dummy", objectiveDisplayName);
        }
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        int currentBalance = (int) getBalance(offlinePlayer);
        int newBalance = currentBalance + (int) amount;
        scoreboard.getObjective(objectiveName).getScore(offlinePlayer.getName()).setScore(newBalance);
        return new EconomyResponse(amount, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    /**
     * @param playerName
     * @param worldName
     * @param amount
     * @deprecated As of VaultAPI 1.4 use {@link #depositPlayer(OfflinePlayer, String, double)} instead.
     */
    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        int currentBalance = (int) getBalance(playerName);
        int newBalance = currentBalance + (int) amount;
        scoreboard.getObjective(objectiveName).getScore(playerName).setScore(newBalance);
        return new EconomyResponse(amount, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    /**
     * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param player    to deposit to
     * @param worldName name of the world
     * @param amount    Amount to deposit
     * @return Detailed response of transaction
     */
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        int currentBalance = (int) getBalance(player);
        int newBalance = currentBalance + (int) amount;
        scoreboard.getObjective(objectiveName).getScore(player.getName()).setScore(newBalance);
        return new EconomyResponse(amount, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    /**
     * @param name
     * @param player
     * @deprecated As of VaultAPI 1.4 use {{@link #createBank(String, OfflinePlayer)} instead.
     */
    @Override
    public EconomyResponse createBank(String name, String player) {
        int balance = (int) getBalance(player);
        return new EconomyResponse(0, balance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    /**
     * Creates a bank account with the specified name and the player as the owner
     *
     * @param name   of account
     * @param player the account should be linked to
     * @return EconomyResponse Object
     */
    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        int balance = (int) getBalance(player);
        return new EconomyResponse(0, balance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    /**
     * Deletes a bank account with the specified name.
     *
     * @param name of the back to delete
     * @return if the operation completed successfully
     */
    @Override
    public EconomyResponse deleteBank(String name) {
        int balance = (int) getBalance(name);
        scoreboard.getObjective(objectiveName).getScore(name).setScore(0);
        return new EconomyResponse(balance * -1, 0, EconomyResponse.ResponseType.SUCCESS, null);
    }

    /**
     * Returns the amount the bank has
     *
     * @param name of the account
     * @return EconomyResponse Object
     */
    @Override
    public EconomyResponse bankBalance(String name) {
        int balance = (int) getBalance(name);
        return new EconomyResponse(0, balance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    /**
     * Returns true or false whether the bank has the amount specified - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param name   of the account
     * @param amount to check for
     * @return EconomyResponse Object
     */
    @Override
    public EconomyResponse bankHas(String name, double amount) {
        int balance = (int) getBalance(name);
        if (balance >= amount) {
            return new EconomyResponse(0, balance, EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(0, balance, EconomyResponse.ResponseType.FAILURE, null);
        }
    }

    /**
     * Withdraw an amount from a bank account - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param name   of the account
     * @param amount to withdraw
     * @return EconomyResponse Object
     */
    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        int currentBalance = (int) getBalance(name);
        if (currentBalance >= amount) {
            int newBalance = currentBalance - (int) amount;
            scoreboard.getObjective(objectiveName).getScore(name).setScore(newBalance);
            return new EconomyResponse(amount, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(0, currentBalance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }
    }

    /**
     * Deposit an amount into a bank account - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param name   of the account
     * @param amount to deposit
     * @return EconomyResponse Object
     */
    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        int currentBalance = (int) getBalance(name);
        int newBalance = currentBalance + (int) amount;
        scoreboard.getObjective(objectiveName).getScore(name).setScore(newBalance);
        return new EconomyResponse(amount, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    /**
     * @param name
     * @param playerName
     * @deprecated As of VaultAPI 1.4 use {{@link #isBankOwner(String, OfflinePlayer)} instead.
     */
    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        if (Objects.equals(name, playerName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
        }
    }

    /**
     * Check if a player is the owner of a bank account
     *
     * @param name   of the account
     * @param player to check for ownership
     * @return EconomyResponse Object
     */
    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        if (Objects.equals(name, player.getName())) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
        }
    }

    /**
     * @param name
     * @param playerName
     * @deprecated As of VaultAPI 1.4 use {{@link #isBankMember(String, OfflinePlayer)} instead.
     */
    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        if (Objects.equals(name, playerName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
        }
    }

    /**
     * Check if the player is a member of the bank account
     *
     * @param name   of the account
     * @param player to check membership
     * @return EconomyResponse Object
     */
    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        if (Objects.equals(name, player.getName())) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
        }
    }

    /**
     * Gets the list of banks
     *
     * @return the List of Banks
     */
    @Override
    public List<String> getBanks() {
        return null;
    }

    /**
     * @param playerName
     * @deprecated As of VaultAPI 1.4 use {{@link #createPlayerAccount(OfflinePlayer)} instead.
     */
    @Override
    public boolean createPlayerAccount(String playerName) {
        return true;
    }

    /**
     * Attempts to create a player account for the given player
     *
     * @param player OfflinePlayer
     * @return if the account creation was successful
     */
    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return true;
    }

    /**
     * @param playerName
     * @param worldName
     * @deprecated As of VaultAPI 1.4 use {{@link #createPlayerAccount(OfflinePlayer, String)} instead.
     */
    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return true;
    }

    /**
     * Attempts to create a player account for the given player on the specified world
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this then false will always be returned.
     *
     * @param player    OfflinePlayer
     * @param worldName String name of the world
     * @return if the account creation was successful
     */
    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return true;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        int currentBalance = (int) getBalance(offlinePlayer);
        if (currentBalance >= amount) {
            int newBalance = currentBalance - (int) amount;
            scoreboard.getObjective(objectiveName).getScore(offlinePlayer.getName()).setScore(newBalance);
            return new EconomyResponse(amount, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(0, currentBalance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }
    }

    /**
     * @param playerName
     * @param worldName
     * @param amount
     * @deprecated As of VaultAPI 1.4 use {@link #withdrawPlayer(OfflinePlayer, String, double)} instead.
     */
    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        int currentBalance = (int) getBalance(playerName);
        if (currentBalance >= amount) {
            int newBalance = currentBalance - (int) amount;
            scoreboard.getObjective(objectiveName).getScore(playerName).setScore(newBalance);
            return new EconomyResponse(amount, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(0, currentBalance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }
    }

    /**
     * Withdraw an amount from a player on a given world - DO NOT USE NEGATIVE AMOUNTS
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param player    to withdraw from
     * @param worldName - name of the world
     * @param amount    Amount to withdraw
     * @return Detailed response of transaction
     */
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        int currentBalance = (int) getBalance(player);
        if (currentBalance >= amount) {
            int newBalance = currentBalance - (int) amount;
            scoreboard.getObjective(objectiveName).getScore(player).setScore(newBalance);
            return new EconomyResponse(amount, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(0, currentBalance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }
    }

    /**
     * @param playerName
     * @param amount
     * @deprecated As of VaultAPI 1.4 use {@link #depositPlayer(OfflinePlayer, double)} instead.
     */
    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        int currentBalance = (int) getBalance(playerName);
        int newBalance = currentBalance + (int) amount;
        scoreboard.getObjective(objectiveName).getScore(playerName).setScore(newBalance);
        return new EconomyResponse(amount, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    /**
     * Checks if economy method is enabled.
     *
     * @return Success or Failure
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Gets name of economy method
     *
     * @return Name of Economy Method
     */
    @Override
    public String getName() {
        return "LyttleScoreboardEconomy";
    }

    /**
     * Returns true if the given implementation supports banks.
     *
     * @return true if the implementation supports banks
     */
    @Override
    public boolean hasBankSupport() {
        return true;
    }

    /**
     * Some economy plugins round off after a certain number of digits.
     * This function returns the number of digits the plugin keeps
     * or -1 if no rounding occurs.
     *
     * @return number of digits after the decimal point kept
     */
    @Override
    public int fractionalDigits() {
        return 0;
    }

    /**
     * Format amount into a human readable String This provides translation into
     * economy specific formatting to improve consistency between plugins.
     *
     * @param amount to format
     * @return Human readable string describing amount
     */
    @Override
    public String format(double amount) {
        return String.valueOf((int) amount);
    }

    /**
     * Returns the name of the currency in plural form.
     * If the economy being used does not support currency names then an empty string will be returned.
     *
     * @return name of the currency (plural)
     */
    @Override
    public String currencyNamePlural() {
        return objectiveDisplayName;
    }

    /**
     * Returns the name of the currency in singular form.
     * If the economy being used does not support currency names then an empty string will be returned.
     *
     * @return name of the currency (singular)
     */
    @Override
    public String currencyNameSingular() {
        return objectiveDisplayName;
    }

    /**
     * @param playerName
     * @deprecated As of VaultAPI 1.4 use {@link #hasAccount(OfflinePlayer)} instead.
     */
    @Override
    public boolean hasAccount(String playerName) {
        return true;
    }

    /**
     * Checks if this player has an account on the server yet
     * This will always return true if the player has joined the server at least once
     * as all major economy plugins auto-generate a player account when the player joins the server
     *
     * @param player to check
     * @return if the player has an account
     */
    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return true;
    }

    /**
     * @param playerName
     * @param worldName
     * @deprecated As of VaultAPI 1.4 use {@link #hasAccount(OfflinePlayer, String)} instead.
     */
    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return true;
    }

    /**
     * Checks if this player has an account on the server yet on the given world
     * This will always return true if the player has joined the server at least once
     * as all major economy plugins auto-generate a player account when the player joins the server
     *
     * @param player    to check in the world
     * @param worldName world-specific account
     * @return if the player has an account
     */
    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return true;
    }

    /**
     * @param playerName
     * @deprecated As of VaultAPI 1.4 use {@link #getBalance(OfflinePlayer)} instead.
     */
    @Override
    public double getBalance(String playerName) {
        int balance = (int) getBalance(playerName);
        return balance;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        Score score = scoreboard.getObjective(objectiveName).getScore(offlinePlayer.getName());
        return score.getScore();
    }

    /**
     * @param playerName
     * @param world
     * @deprecated As of VaultAPI 1.4 use {@link #getBalance(OfflinePlayer, String)} instead.
     */
    @Override
    public double getBalance(String playerName, String world) {
        int balance = (int) getBalance(playerName);
        return balance;
    }

    /**
     * Gets balance of a player on the specified world.
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param player to check
     * @param world  name of the world
     * @return Amount currently held in players account
     */
    @Override
    public double getBalance(OfflinePlayer player, String world) {
        int balance = (int) getBalance(player.getName());
        return balance;
    }

    /**
     * @param playerName
     * @param amount
     * @deprecated As of VaultAPI 1.4 use {@link #has(OfflinePlayer, double)} instead.
     */
    @Override
    public boolean has(String playerName, double amount) {
        int balance = (int) getBalance(playerName);
        return balance >= amount;
    }

    /**
     * Checks if the player account has the amount - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param player to check
     * @param amount to check for
     * @return True if <b>player</b> has <b>amount</b>, False else wise
     */
    @Override
    public boolean has(OfflinePlayer player, double amount) {
        int balance = (int) getBalance(player);
        return balance >= amount;
    }

    /**
     * @param playerName
     * @param worldName
     * @param amount
     * @deprecated As of VaultAPI 1.4 use @{link {@link #has(OfflinePlayer, String, double)} instead.
     */
    @Override
    public boolean has(String playerName, String worldName, double amount) {
        int balance = (int) getBalance(playerName);
        return balance >= amount;
    }

    /**
     * Checks if the player account has the amount in a given world - DO NOT USE NEGATIVE AMOUNTS
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param player    to check
     * @param worldName to check with
     * @param amount    to check for
     * @return True if <b>player</b> has <b>amount</b>, False else wise
     */
    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        int balance = (int) getBalance(player);
        return balance >= amount;
    }

    /**
     * @param playerName
     * @param amount
     * @deprecated As of VaultAPI 1.4 use {@link #withdrawPlayer(OfflinePlayer, double)} instead.
     */
    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        int currentBalance = (int) getBalance(playerName);
        if (currentBalance >= amount) {
            int newBalance = currentBalance - (int) amount;
            scoreboard.getObjective(objectiveName).getScore(playerName).setScore(newBalance);
            return new EconomyResponse(amount, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(0, currentBalance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }
    }
}
