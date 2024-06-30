# LyttleScoreboardEconomy

LyttleScoreboardEconomy is a Minecraft plugin that provides an economy system based on the player's score. It's built with Java and Maven, and uses the Vault API for economy interactions.

## Features

- Player balance is stored and managed as a score.
- Compatibility with Vault API 1.4.
- Supports multi-world economy.
- Provides a `baltop` command to display the top players by balance.
- Provides a `tokens` command to interact with the player's balance.
- Provides an API for interacting with the player's balance.

## Installation

1. Download the latest release from the GitHub releases page.
2. Place the downloaded `.jar` file into your server's `plugins` directory.
3. Restart your server.

## Usage

The plugin works automatically once installed. It integrates with Vault, so any plugin that uses Vault for economy transactions will be able to use LyttleScoreboardEconomy.

## Commands

Here are the commands that players can use with LyttleScoreboardEconomy:

- `/baltop [page]`: Displays the top players by balance. The optional `page` argument can be used to navigate through the list.
- `/tokens`: Displays the player's current balance.
- `/tokens send <player> <amount>`: Sends the specified amount of tokens from the command sender to the specified player.
- `/tokens balance [player]`: Displays the specified player's balance. If no player is specified, it displays the command sender's balance.

Please note that the `tokens` command can only be run by a player.
## API

LyttleScoreboardEconomy provides the following methods for interacting with a player's balance:

- `getBalance(OfflinePlayer player)`: Returns the balance of the specified player.
- `has(OfflinePlayer player, double amount)`: Checks if the player has at least the specified amount.
- `withdrawPlayer(OfflinePlayer player, double amount)`: Withdraws the specified amount from the player's balance.

## Contributing

Contributions are welcome! Please read the [contributing guidelines](CONTRIBUTING.md) before getting started.

# License

All rights reserved. Before using or distributing this software, you must first obtain permission from the author. Please contact the author (Stualyttle Kirry) at [Discord](https://discord.com/invite/QfqFFPFFQZ) to request permission.