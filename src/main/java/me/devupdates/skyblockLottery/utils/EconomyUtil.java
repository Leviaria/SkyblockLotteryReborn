package me.devupdates.skyblockLottery.utils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyUtil {
    
    private final JavaPlugin plugin;
    private Economy economy = null;
    private boolean vaultEnabled = false;
    
    public EconomyUtil(JavaPlugin plugin) {
        this.plugin = plugin;
        setupEconomy();
    }
    
    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().info("Vault not found, economy support disabled.");
            return false;
        }
        
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().info("No economy provider found, economy support disabled.");
            return false;
        }
        
        economy = rsp.getProvider();
        vaultEnabled = economy != null;
        
        if (vaultEnabled) {
            plugin.getLogger().info("Vault economy support enabled with " + economy.getName());
        }
        
        return vaultEnabled;
    }
    
    public boolean isEnabled() {
        return vaultEnabled;
    }
    
    public boolean hasEnough(String playerName, double amount) {
        if (!vaultEnabled || economy == null) {
            return false;
        }
        return economy.has(playerName, amount);
    }
    
    public boolean withdraw(String playerName, double amount) {
        if (!vaultEnabled || economy == null) {
            return false;
        }
        return economy.withdrawPlayer(playerName, amount).transactionSuccess();
    }
    
    public boolean deposit(String playerName, double amount) {
        if (!vaultEnabled || economy == null) {
            return false;
        }
        return economy.depositPlayer(playerName, amount).transactionSuccess();
    }
    
    public double getBalance(String playerName) {
        if (!vaultEnabled || economy == null) {
            return 0.0;
        }
        return economy.getBalance(playerName);
    }
    
    public String getCurrencyName() {
        if (!vaultEnabled || economy == null) {
            return "Money";
        }
        return economy.currencyNamePlural();
    }
} 