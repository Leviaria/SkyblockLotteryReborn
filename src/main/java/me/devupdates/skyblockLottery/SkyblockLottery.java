package me.devupdates.skyblockLottery;

import me.devupdates.skyblockLottery.commands.LotteryCommand;
import me.devupdates.skyblockLottery.config.ConfigManager;
import me.devupdates.skyblockLottery.config.MessagesManager;
import me.devupdates.skyblockLottery.lottery.LotteryManager;
import me.devupdates.skyblockLottery.utils.EconomyUtil;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class SkyblockLottery extends JavaPlugin {

    private static SkyblockLottery instance;
    private ConfigManager configManager;
    private MessagesManager messagesManager;
    private LotteryManager lotteryManager;
    private EconomyUtil economyUtil;
    private Logger logger;


    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        
        logger.info("Starting SkyblockLottery Reborn...");
        
        // Initialize managers
        configManager = new ConfigManager(this);
        messagesManager = new MessagesManager(this);
        economyUtil = new EconomyUtil(this);
        lotteryManager = new LotteryManager(this);
        
        // Register commands
        getCommand("lottery").setExecutor(new LotteryCommand(this));
        
        logger.info("SkyblockLottery Reborn has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (lotteryManager != null) {
            lotteryManager.shutdown();
        }
        
        logger.info("SkyblockLottery Reborn has been disabled!");
    }
    

    
    public void reload() {
        logger.info("Reloading SkyblockLottery...");
        
        configManager.reload();
        messagesManager.reload();
        lotteryManager.reload();
        
        logger.info("SkyblockLottery reloaded successfully!");
    }
    
    // Getters
    public static SkyblockLottery getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public MessagesManager getMessagesManager() {
        return messagesManager;
    }
    
    public LotteryManager getLotteryManager() {
        return lotteryManager;
    }
    
    public EconomyUtil getEconomyUtil() {
        return economyUtil;
    }
} 