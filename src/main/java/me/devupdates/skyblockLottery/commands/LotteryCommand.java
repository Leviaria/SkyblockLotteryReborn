package me.devupdates.skyblockLottery.commands;

import me.devupdates.skyblockLottery.SkyblockLottery;
import me.devupdates.skyblockLottery.config.MessagesManager;
import me.devupdates.skyblockLottery.lottery.LotteryManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LotteryCommand implements CommandExecutor {
    
    private final SkyblockLottery plugin;
    private final LotteryManager lotteryManager;
    private final MessagesManager messagesManager;
    
    public LotteryCommand(SkyblockLottery plugin) {
        this.plugin = plugin;
        this.lotteryManager = plugin.getLotteryManager();
        this.messagesManager = plugin.getMessagesManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(messagesManager.getUsage());
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "buy":
                return handleBuyCommand(sender, args);
            case "start":
                return handleStartCommand(sender);
            case "claim":
                return handleClaimCommand(sender);
            case "reload":
                return handleReloadCommand(sender);
            case "info":
                return handleInfoCommand(sender);
            default:
                sender.sendMessage(messagesManager.getUsage());
                return true;
        }
    }
    
    private boolean handleBuyCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be executed by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("lottery.buy")) {
            player.sendMessage(messagesManager.getNoPermission());
            return true;
        }
        
        if (args.length < 2) {
            player.sendMessage("§cUsage: /lottery buy <amount>");
            return true;
        }
        
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(messagesManager.getInvalidAmount());
            return true;
        }
        
        lotteryManager.buyTickets(player, amount);
        return true;
    }
    
    private boolean handleStartCommand(CommandSender sender) {
        if (!sender.hasPermission("lottery.start")) {
            sender.sendMessage(messagesManager.getNoPermission());
            return true;
        }
        
        lotteryManager.forceStart();
        sender.sendMessage(messagesManager.getLotteryForceStarted());
        
        plugin.getLogger().info(sender.getName() + " manually started the lottery.");
        return true;
    }
    
    private boolean handleClaimCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be executed by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("lottery.claim")) {
            player.sendMessage(messagesManager.getNoPermission());
            return true;
        }
        
        lotteryManager.claimWinnings(player);
        return true;
    }
    
    private boolean handleReloadCommand(CommandSender sender) {
        if (!sender.hasPermission("lottery.reload")) {
            sender.sendMessage(messagesManager.getNoPermission());
            return true;
        }
        
        plugin.reload();
        sender.sendMessage(messagesManager.getReloadSuccess());
        
        plugin.getLogger().info(sender.getName() + " reloaded the plugin configuration.");
        return true;
    }
    
    private boolean handleInfoCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be executed by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (lotteryManager.isActive()) {
            int tickets = lotteryManager.getPlayerTickets(player.getUniqueId());
            player.sendMessage("§e=== Lottery Info ===");
            player.sendMessage("§aStatus: §eActive");
            player.sendMessage("§aYour Tickets: §b" + tickets);
            player.sendMessage("§aMax Tickets: §b" + plugin.getConfigManager().getMaxTicketsPerPlayer());
        } else {
            player.sendMessage("§e=== Lottery Info ===");
            player.sendMessage("§cStatus: §eInactive");
        }
        
        if (lotteryManager.hasWinnings(player.getUniqueId())) {
            player.sendMessage("§a§lYou have unclaimed winnings! Use /lottery claim");
        }
        
        return true;
    }
} 