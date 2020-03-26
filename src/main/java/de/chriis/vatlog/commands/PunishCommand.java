package de.chriis.vatlog.commands;

import de.chriis.vatlog.VatlogPanel;
import de.chriis.vatlog.utils.InventoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PunishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (args.length == 1) {
                String username = args[0];

                if (player.hasPermission("vatlog.panel.view")) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);

                    if (offlinePlayer == null) {
                        player.sendMessage(VatlogPanel.prefix + "This player could not be found");
                        return false;
                    }

                    InventoryBuilder.openBanInventory(player, offlinePlayer, 1);
                } else {
                    player.sendMessage(VatlogPanel.noPermission);
                }
            } else {
                player.sendMessage(getHelper(label));
            }
        }

        return false;
    }

    private String getHelper(String label) {
        return VatlogPanel.prefix + "Command usage: §e/" + label + " <player>";
    }
}
