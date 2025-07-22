package me.devupdates.skyblockLottery.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
    
    private final JavaPlugin plugin;
    private FileConfiguration config;
    
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }
    
    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
    }
    
    public void reload() {
        loadConfig();
    }
    
    // General Settings
    public String getPrefix() {
        return config.getString("general.prefix", "&8[&eSkyblockLottery-Reborn&8]");
    }
    
    // Lottery Settings
    public int getLotteryIntervalMinutes() {
        return config.getInt("lottery.interval-minutes", 20);
    }
    
    public int getMaxTicketsPerPlayer() {
        return config.getInt("lottery.max-tickets-per-player", 128);
    }
    
    public String getCurrencyMaterial() {
        return config.getString("lottery.currency.material", "COBBLESTONE");
    }
    
    public int getCurrencyAmount() {
        return config.getInt("lottery.currency.amount", 1);
    }
    
    public boolean isEconomyEnabled() {
        return config.getBoolean("lottery.economy.enabled", false);
    }
    
    public double getEconomyCost() {
        return config.getDouble("lottery.economy.cost", 10.0);
    }
    
    // Rewards
    public int getMinRewardAmount() {
        return config.getInt("lottery.rewards.min-amount", 64);
    }
    
    public int getMaxRewardAmount() {
        return config.getInt("lottery.rewards.max-amount", 512);
    }
    
    public String getRewardMaterial() {
        return config.getString("lottery.rewards.material", "COBBLESTONE");
    }
    
    // Auto-start
    public boolean isAutoStartEnabled() {
        return config.getBoolean("lottery.auto-start.enabled", true);
    }
    

} 