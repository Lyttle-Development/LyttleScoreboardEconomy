<div align="center">
  
# Lyttle Scoreboard Economy

[![Paper](https://img.shields.io/badge/Paper-1.21.x-blue)](https://papermc.io)
[![Hangar](https://img.shields.io/badge/Hangar-download-success)](https://hangar.papermc.io/Lyttle-Development)
[![Discord](https://img.shields.io/discord/941334383216967690?color=7289DA&label=Discord&logo=discord&logoColor=ffffff)](https://discord.gg/QfqFFPFFQZ)

> ✨ **A lightweight scoreboard-based economy system for your Minecraft server!** ✨

[📚 Features](#--features) • [⌨️ Commands](#-%EF%B8%8F-commands) • [🔑 Permissions](#--permissions) • [📥 Installation](#--installation) • [⚙️ Configuration](#%EF%B8%8F-configuration) • [📱 Support](#--support)

</div>

![Divider](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)

## 🌟 Features

### 🎯 Core Plugin Features
- Scoreboard-based token economy system
- Player-to-player token transfers
- Real-time balance checking
- Simple and intuitive command system
- MiniMessage formatting support for all messages

---

### 🤌 Lyttle Certified
- Basic plugin without fluff
- No unnecessary features
- Full flexibility and configurability
- Open source and free to use (MIT License)

---

## ⌨️ Commands

> 💡 `<required>` `[optional]`

| Command                    | Permission         | Description                     |
|:--------------------------|:-------------------|:--------------------------------|
| `/tokens`                 | `tokens.balance`   | Check your token balance       |
| `/tokens balance [player]`| `tokens.balance`   | Check another player's balance |
| `/tokens send <player> <amount>` | `tokens.send` | Send tokens to another player |

---

## 🔑 Permissions

| Permission Node    | Description                            | Default |
|:------------------|:---------------------------------------|:--------|
| `tokens.*`        | Grants all plugin permissions          | `❌`     |
| `tokens.balance`  | Allows checking token balances         | `✔️`    |
| `tokens.send`     | Allows sending tokens to other players | `✔️`    |

---

## 📥 Installation

### Quick Start
1. Download the latest version from [Hangar](https://hangar.papermc.io/Lyttle-Development)
2. Place the `.jar` file in your server's `plugins` folder
3. Restart your server
4. Edit the configuration file to customize the scoreboard settings
5. Use `/tokens` to start managing your economy

---

### 📋 Requirements
- Java 21 or newer
- Paper 1.21.x+
- Minimum 10MB free disk space

---

### 💫 Dependencies
- None! This plugin works standalone

---

### 📝 Configuration Files
#### 🔧 `config.yml`
```yaml 
scoreboard_objective: "tokens" 
scoreboard_objective_name: "Tokens"
``` 

#### 💬 `messages.yml`
Customize all plugin messages with MiniMessage formatting support.

### 🔄 The #defaults Folder
The folder serves several important purposes: `#defaults`
1. **Backup Reference**: Contains original copies of all configuration files
2. **Reset Option**: Use these to restore default settings
3. **Update Safety**: Preserved during plugin updates
4. **Documentation**: Shows all available options with comments


> 💡 **Never modify files in the #defaults folder!** They are automatically overwritten during server restarts.
>
---

## 💬 Support

<div align="center">

### 🤝 Need Help?

[![Discord](https://img.shields.io/discord/941334383216967690?color=7289DA&label=Join%20Our%20Discord&logo=discord&logoColor=ffffff&style=for-the-badge)](https://discord.gg/QfqFFPFFQZ)

🐛 Found a bug? [Open an Issue](https://github.com/Lyttle-Development/LyttleScoreboardEconomy/issues)  
💡 Have a suggestion? [Share your idea](https://github.com/Lyttle-Development/LyttleScoreboardEconomy/issues)

</div>

---

## 📜 License

<div align="center">

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

### 🌟 Made with the lyttlest details in mind by [Lyttle Development](https://www.lyttledevelopment.com)

If you enjoy this plugin, please consider:

⭐ Giving it a star on GitHub <br>
💬 Sharing it with other server owners<br>
🎁 Supporting development through [Donations](https://github.com/LyttleDevelopment)

![Divider](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)

</div>
