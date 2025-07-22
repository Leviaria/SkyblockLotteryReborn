package me.devupdates.skyblockLottery.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MessagesManager {
    
    private final JavaPlugin plugin;
    private File messagesFile;
    private FileConfiguration messagesConfig;
    
    public MessagesManager(JavaPlugin plugin) {
        this.plugin = plugin;
        setupMessages();
    }
    
    private void setupMessages() {
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
        
        // Load defaults from jar
        InputStream defConfigStream = plugin.getResource("messages.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            messagesConfig.setDefaults(defConfig);
        }
    }
    
    public void reload() {
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
        
        InputStream defConfigStream = plugin.getResource("messages.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            messagesConfig.setDefaults(defConfig);
        }
    }
    
    public void save() {
        try {
            messagesConfig.save(messagesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save messages.yml: " + e.getMessage());
        }
    }
    
    private String formatMessage(String key, String defaultMessage) {
        String message = messagesConfig.getString(key, defaultMessage);
        message = ChatColor.translateAlternateColorCodes('&', message);
        
        // Add configurable prefix
        me.devupdates.skyblockLottery.SkyblockLottery skyblockLottery = (me.devupdates.skyblockLottery.SkyblockLottery) plugin;
        String prefix = skyblockLottery.getConfigManager().getPrefix();
        prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        message = message.replace("{prefix}", prefix);
        
        return message;
    }
    
    // Lottery Messages
    public String getLotteryStarted() {
        return formatMessage("lottery.started", "&a[{timestamp}] &eEine neue Lotterie hat begonnen! Kaufe Tickets mit /lottery buy <anzahl>");
    }
    
    public String getLotteryEnded(String winner, int amount) {
        String message = formatMessage("lottery.ended", "&a[{timestamp}] &eLotterie beendet! Gewinner: &b{winner} &emit &a{amount}x {material}");
        return message.replace("{winner}", winner).replace("{amount}", String.valueOf(amount));
    }
    
    public String getTicketBought(int amount, int total) {
        String message = formatMessage("lottery.ticket-bought", "&a[{timestamp}] &eTicket gekauft! Du hast jetzt &b{total} &eTickets (&a+{amount}&e)");
        return message.replace("{amount}", String.valueOf(amount)).replace("{total}", String.valueOf(total));
    }
    
    public String getNotEnoughCurrency() {
        return formatMessage("lottery.not-enough-currency", "&c[{timestamp}] &cDu hast nicht genügend Cobblestone!");
    }
    
    public String getMaxTicketsReached() {
        return formatMessage("lottery.max-tickets", "&c[{timestamp}] &cDu hast bereits das Maximum an Tickets erreicht!");
    }
    
    public String getNoActiveLottery() {
        return formatMessage("lottery.no-active", "&c[{timestamp}] &cEs läuft derzeit keine Lotterie.");
    }
    
    public String getClaimSuccess(int amount, String material) {
        String message = formatMessage("lottery.claim-success", "&a[{timestamp}] &eGewinn abgeholt: &a{amount}x {material}");
        return message.replace("{amount}", String.valueOf(amount)).replace("{material}", material);
    }
    
    public String getNoWinnings() {
        return formatMessage("lottery.no-winnings", "&c[{timestamp}] &cDu hast keine Gewinne abzuholen.");
    }
    
    public String getInventoryFull() {
        return formatMessage("lottery.inventory-full", "&c[{timestamp}] &cDein Inventar ist voll! Gewinne wurden auf den Boden gedroppt.");
    }
    
    // Command Messages
    public String getUsage() {
        return formatMessage("commands.usage", "&c[{timestamp}] &eNutzung: /lottery <start|buy|claim|reload>");
    }
    
    public String getNoPermission() {
        return formatMessage("commands.no-permission", "&c[{timestamp}] &cDu hast keine Berechtigung für diesen Befehl!");
    }
    
    public String getReloadSuccess() {
        return formatMessage("commands.reload-success", "&a[{timestamp}] &eKonfiguration erfolgreich neu geladen!");
    }
    
    public String getInvalidAmount() {
        return formatMessage("commands.invalid-amount", "&c[{timestamp}] &cUngültige Anzahl! Nutze eine Zahl zwischen 1 und 64.");
    }
    
    public String getLotteryForceStarted() {
        return formatMessage("lottery.force-started", "&a[{timestamp}] &eLotterie wurde manuell gestartet!");
    }
} 