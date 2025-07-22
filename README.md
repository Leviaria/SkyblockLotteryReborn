# 🎰 SkyblockLottery Reborn

[![Java](https://img.shields.io/badge/Java-21+-orange.svg)](https://openjdk.org/)
[![Server](https://img.shields.io/badge/Server-Leaf%201.21.8-blue.svg)](https://leafmc.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Version](https://img.shields.io/badge/Version-1.0-brightgreen.svg)](https://github.com/yourusername/SkyblockLottery-Reborn/releases)

> **A modern, feature-rich lottery plugin designed for Skyblock servers**

Transform your Skyblock server with an engaging lottery system that keeps players coming back! SkyblockLottery Reborn offers a completely configurable lottery experience using Cobblestone (or any material) as currency.

---

## ✨ Features

### 🎯 **Fully Configurable Lottery System**
- Customizable lottery intervals (default: 20 minutes)
- Adjustable ticket limits per player (default: 128)
- Configurable currency material and amounts
- Random reward ranges (64-512 Cobblestone by default)

### ⚙️ **Modern Configuration System**
- Easy-to-use YAML configuration files
- Customizable message system with color codes
- Configurable plugin prefix for all messages
- Hot-reload functionality (`/lottery reload`)

### 💰 **Multiple Currency Options**
- **Item-based:** Use Cobblestone, Diamonds, or any material
- **Economy Support:** Optional Vault integration for money-based tickets
- Seamless switching between currency types

### 🔐 **Advanced Permission System**
- `lottery.buy` - Purchase lottery tickets (default: all players)
- `lottery.start` - Manually start lottery (default: operators)
- `lottery.claim` - Claim winnings (default: all players)
- `lottery.reload` - Reload configuration (default: operators)

---

## 🚀 What's New in Reborn?

- **🎨 Customizable Messages:** Every message can be personalized in `messages.yml`
- **🔧 Hot Reloading:** No server restart needed for config changes
- **⚡ Modern Performance:** Built for Leaf 1.21.8 with optimal efficiency
- **🎪 Smart Inventory Management:** Full inventory protection with item dropping
- **🎯 Weighted Lottery System:** More tickets = higher winning chances
- **🛡️ Developer-Friendly:** Clean API ready for extensions

---

## 📦 Installation

### Requirements
- **Java:** 21+
- **Server:** Leaf 1.21.8 or compatible
- **Optional:** Vault plugin (for economy support)

### Setup
1. Download the latest release from [Releases](https://github.com/yourusername/SkyblockLottery-Reborn/releases)
2. Drop `SkyblockLotteryReborn-1.0.jar` into your `plugins/` folder
3. Start/restart your server
4. Customize `config.yml` and `messages.yml` in the plugin folder
5. Use `/lottery reload` to apply changes
6. Players can start buying tickets with `/lottery buy <amount>`!

---

## 🎮 Commands & Usage

| Command | Description | Permission | Default |
|---------|-------------|------------|---------|
| `/lottery buy <amount>` | Purchase lottery tickets | `lottery.buy` | All players |
| `/lottery start` | Force start a new lottery | `lottery.start` | Operators |
| `/lottery claim` | Claim your winnings | `lottery.claim` | All players |
| `/lottery info` | View lottery status and tickets | None | All players |
| `/lottery reload` | Reload plugin configuration | `lottery.reload` | Operators |

### Example Usage
```bash
# Buy 10 lottery tickets
/lottery buy 10

# Check current lottery status
/lottery info

# Claim your winnings
/lottery claim

# Force start a new lottery (admin only)
/lottery start

# Reload configuration (admin only)
/lottery reload
```

---

## ⚙️ Configuration

### `config.yml`
```yaml
# General Settings
general:
  # Prefix for all plugin messages
  prefix: "&8[&eSkyblockLottery-Reborn&8]"

# Lottery Settings
lottery:
  # Auto-start lottery every X minutes
  interval-minutes: 20
  
  # Maximum tickets a player can buy per lottery
  max-tickets-per-player: 128
  
  # Currency settings (when economy is disabled)
  currency:
    material: "COBBLESTONE"
    amount: 1
  
  # Economy settings (requires Vault plugin)
  economy:
    enabled: false
    cost: 10.0
  
  # Reward settings
  rewards:
    min-amount: 64
    max-amount: 512
    material: "COBBLESTONE"
  
  # Auto-start settings
  auto-start:
    enabled: true
```

### `messages.yml`
All messages are fully customizable with color codes and placeholders:
```yaml
lottery:
  started: "{prefix} &eA new lottery has started! Buy tickets with /lottery buy <amount>"
  ended: "{prefix} &eLottery ended! Winner: &b{winner} &ewith &a{amount}x {material}"
  ticket-bought: "{prefix} &eTicket purchased! You now have &b{total} &etickets (&a+{amount}&e)"
  # ... and many more!
```

---

## 🛠️ Development

### Building from Source
```bash
# Clone the repository
git clone https://github.com/yourusername/SkyblockLottery-Reborn.git
cd SkyblockLottery-Reborn

# Build with Gradle
./gradlew clean build

# JAR will be in build/libs/
```

### API Usage
The plugin provides a clean API for developers:
```java
// Get the lottery manager
LotteryManager lotteryManager = SkyblockLottery.getInstance().getLotteryManager();

// Check if lottery is active
boolean isActive = lotteryManager.isActive();

// Get player's ticket count
int tickets = lotteryManager.getPlayerTickets(player.getUniqueId());

// Check if player has winnings
boolean hasWinnings = lotteryManager.hasWinnings(player.getUniqueId());
```

---

## 💡 Perfect For

- **🏝️ Skyblock Servers** - Keep players engaged with regular lottery events
- **🎮 Survival Servers** - Add excitement to resource collection
- **🏆 Competition Servers** - Create stakes and rewards for achievements
- **🎪 Event Servers** - Automated reward distribution system

---

## 📊 Statistics

- **Efficient Performance:** Lightweight with minimal server impact
- **Memory Safe:** Smart memory management and cleanup
- **Thread Safe:** Concurrent operations handled properly
- **Database Ready:** Easy integration with data persistence

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🆘 Support

- **Issues:** [GitHub Issues](https://github.com/yourusername/SkyblockLottery-Reborn/issues)
- **Wiki:** [Plugin Wiki](https://github.com/yourusername/SkyblockLottery-Reborn/wiki)
- **Downloads:** [CurseForge](https://www.curseforge.com/minecraft/bukkit-plugins/skyblockLottery-reborn)

---

## 🙏 Acknowledgments

- Original SkyblockLottery concept
- The Minecraft server development community
- All contributors and users

---

<div align="center">

**⭐ Star this repository if you found it helpful! ⭐**

**Transform your server economy and player engagement with SkyblockLottery Reborn!**

*The modern evolution of lottery plugins* ✨

</div> 