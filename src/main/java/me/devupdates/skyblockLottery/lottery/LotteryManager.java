package me.devupdates.skyblockLottery.lottery;

import me.devupdates.skyblockLottery.SkyblockLottery;
import me.devupdates.skyblockLottery.config.ConfigManager;
import me.devupdates.skyblockLottery.config.MessagesManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class LotteryManager {
    
    private final SkyblockLottery plugin;
    private final ConfigManager configManager;
    private final MessagesManager messagesManager;
    
    private boolean lotteryActive = false;
    private final Map<UUID, Integer> playerTickets = new HashMap<>();
    private final Map<UUID, Integer> playerWinnings = new HashMap<>();
    private BukkitTask lotteryTask;
    
    public LotteryManager(SkyblockLottery plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.messagesManager = plugin.getMessagesManager();
        
        if (configManager.isAutoStartEnabled()) {
            startLotteryTimer();
        }
    }
    
    public void startLotteryTimer() {
        if (lotteryTask != null) {
            lotteryTask.cancel();
        }
        
        int intervalMinutes = configManager.getLotteryIntervalMinutes();
        long intervalTicks = intervalMinutes * 60 * 20L; // Convert minutes to ticks
        
        lotteryTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (lotteryActive) {
                    endLottery();
                } else {
                    startLottery();
                }
            }
        }.runTaskTimer(plugin, 0L, intervalTicks);
        
        plugin.getLogger().info("Lottery timer started with " + intervalMinutes + " minute intervals.");
    }
    
    public void startLottery() {
        if (lotteryActive) {
            return;
        }
        
        lotteryActive = true;
        playerTickets.clear();
        
        // Broadcast start message
        String startMessage = messagesManager.getLotteryStarted();
        Bukkit.broadcastMessage(startMessage);
        
        plugin.getLogger().info("New lottery started!");
    }
    
    public void endLottery() {
        if (!lotteryActive) {
            return;
        }
        
        lotteryActive = false;
        
        if (playerTickets.isEmpty()) {
            Bukkit.broadcastMessage("§7Lottery ended - no participants.");
            plugin.getLogger().info("Lottery ended with no participants.");
            return;
        }
        
        // Calculate winner
        UUID winner = selectWinner();
        if (winner == null) {
            Bukkit.broadcastMessage("§cError selecting lottery winner!");
            plugin.getLogger().warning("Failed to select lottery winner!");
            return;
        }
        
        // Calculate reward
        int rewardAmount = calculateReward();
        String rewardMaterial = configManager.getRewardMaterial();
        
        // Store winnings for claim
        playerWinnings.put(winner, rewardAmount);
        
        // Get winner name
        String winnerName = Bukkit.getOfflinePlayer(winner).getName();
        if (winnerName == null) winnerName = "Unknown";
        
        // Broadcast end message
        String endMessage = messagesManager.getLotteryEnded(winnerName, rewardAmount);
        endMessage = endMessage.replace("{material}", rewardMaterial);
        Bukkit.broadcastMessage(endMessage);
        
        plugin.getLogger().info("Lottery ended! Winner: " + winnerName + " with " + rewardAmount + "x " + rewardMaterial);
    }
    
    private UUID selectWinner() {
        List<UUID> weightedList = new ArrayList<>();
        
        // Add players based on their ticket count (more tickets = higher chance)
        for (Map.Entry<UUID, Integer> entry : playerTickets.entrySet()) {
            UUID playerId = entry.getKey();
            int tickets = entry.getValue();
            
            for (int i = 0; i < tickets; i++) {
                weightedList.add(playerId);
            }
        }
        
        if (weightedList.isEmpty()) {
            return null;
        }
        
        Random random = new Random();
        return weightedList.get(random.nextInt(weightedList.size()));
    }
    
    private int calculateReward() {
        int minReward = configManager.getMinRewardAmount();
        int maxReward = configManager.getMaxRewardAmount();
        
        if (minReward >= maxReward) {
            return minReward;
        }
        
        Random random = new Random();
        return random.nextInt(maxReward - minReward + 1) + minReward;
    }
    
    public boolean buyTickets(Player player, int amount) {
        if (!lotteryActive) {
            player.sendMessage(messagesManager.getNoActiveLottery());
            return false;
        }
        
        if (amount <= 0 || amount > 64) {
            player.sendMessage(messagesManager.getInvalidAmount());
            return false;
        }
        
        UUID playerId = player.getUniqueId();
        int currentTickets = playerTickets.getOrDefault(playerId, 0);
        int maxTickets = configManager.getMaxTicketsPerPlayer();
        
        if (currentTickets + amount > maxTickets) {
            player.sendMessage(messagesManager.getMaxTicketsReached());
            return false;
        }
        
        // Check payment method
        if (configManager.isEconomyEnabled() && plugin.getEconomyUtil().isEnabled()) {
            // Use economy
            double cost = configManager.getEconomyCost() * amount;
            if (!plugin.getEconomyUtil().hasEnough(player.getName(), cost)) {
                player.sendMessage(messagesManager.getNotEnoughCurrency());
                return false;
            }
            
            if (!plugin.getEconomyUtil().withdraw(player.getName(), cost)) {
                player.sendMessage("§cError withdrawing money!");
                return false;
            }
        } else {
            // Use items
            Material currency = Material.valueOf(configManager.getCurrencyMaterial());
            int currencyAmount = configManager.getCurrencyAmount() * amount;
            
            if (!hasEnoughItems(player, currency, currencyAmount)) {
                player.sendMessage(messagesManager.getNotEnoughCurrency());
                return false;
            }
            
            removeItems(player, currency, currencyAmount);
        }
        
        // Add tickets
        playerTickets.put(playerId, currentTickets + amount);
        
        // Send success message
        int newTotal = currentTickets + amount;
        player.sendMessage(messagesManager.getTicketBought(amount, newTotal));
        
        plugin.getLogger().info(player.getName() + " bought " + amount + " tickets (total: " + newTotal + ")");
        return true;
    }
    
    private boolean hasEnoughItems(Player player, Material material, int amount) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                count += item.getAmount();
                if (count >= amount) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void removeItems(Player player, Material material, int amount) {
        int remaining = amount;
        
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.getType() == material) {
                int itemAmount = item.getAmount();
                
                if (itemAmount <= remaining) {
                    player.getInventory().setItem(i, null);
                    remaining -= itemAmount;
                } else {
                    item.setAmount(itemAmount - remaining);
                    remaining = 0;
                }
                
                if (remaining <= 0) {
                    break;
                }
            }
        }
        
        player.updateInventory();
    }
    
    public boolean claimWinnings(Player player) {
        UUID playerId = player.getUniqueId();
        if (!playerWinnings.containsKey(playerId)) {
            player.sendMessage(messagesManager.getNoWinnings());
            return false;
        }
        
        int amount = playerWinnings.get(playerId);
        Material material = Material.valueOf(configManager.getRewardMaterial());
        ItemStack reward = new ItemStack(material, amount);
        
        // Try to add to inventory
        HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(reward);
        
        if (!leftover.isEmpty()) {
            // Drop items on ground if inventory is full
            for (ItemStack item : leftover.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
            player.sendMessage(messagesManager.getInventoryFull());
        }
        
        // Remove from winnings
        playerWinnings.remove(playerId);
        
        player.sendMessage(messagesManager.getClaimSuccess(amount, material.name()));
        plugin.getLogger().info(player.getName() + " claimed winnings: " + amount + "x " + material.name());
        
        return true;
    }
    
    public boolean isActive() {
        return lotteryActive;
    }
    
    public int getPlayerTickets(UUID playerId) {
        return playerTickets.getOrDefault(playerId, 0);
    }
    
    public boolean hasWinnings(UUID playerId) {
        return playerWinnings.containsKey(playerId);
    }
    
    public void forceStart() {
        if (lotteryActive) {
            endLottery();
        }
        startLottery();
    }
    
    public void reload() {
        if (configManager.isAutoStartEnabled()) {
            startLotteryTimer();
        } else if (lotteryTask != null) {
            lotteryTask.cancel();
            lotteryTask = null;
        }
    }
    
    public void shutdown() {
        if (lotteryTask != null) {
            lotteryTask.cancel();
        }
        
        if (lotteryActive) {
            // Save current state or end lottery gracefully
            endLottery();
        }
    }
} 