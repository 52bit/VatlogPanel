package de.chriis.vatlog.listener;

import de.chriis.vatlog.VatlogPanel;
import de.chriis.vatlog.bans.Ban;
import de.chriis.vatlog.bans.BanManager;
import de.chriis.vatlog.mutes.Mute;
import de.chriis.vatlog.mutes.MuteManager;
import de.chriis.vatlog.reasons.Reason;
import de.chriis.vatlog.utils.InventoryBuilder;
import de.chriis.vatlog.utils.PanelManager;
import javafx.scene.layout.Pane;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            String banInventoryName = VatlogPanel.getInstance().getConfig().getString("inventory.bantitle");
            String muteInventoryName = VatlogPanel.getInstance().getConfig().getString("inventory.mutetitle");

            String lastPage = VatlogPanel.getInstance().getConfig().getString("meta.lastPage");
            String nextPage = VatlogPanel.getInstance().getConfig().getString("meta.nextPage");
            String viewBans = VatlogPanel.getInstance().getConfig().getString("meta.viewBans");
            String viewMutes = VatlogPanel.getInstance().getConfig().getString("meta.viewMutes");
            String neverBanned = VatlogPanel.getInstance().getConfig().getString("meta.neverBanned");
            String neverMuted = VatlogPanel.getInstance().getConfig().getString("meta.neverMuted");

            // Check if Inventory is BAN Panel
            if(event.getInventory().getName().equalsIgnoreCase(banInventoryName)) {
                event.setCancelled(true);

                if(event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
                    Reason reason = Reason.fromItemName(event.getCurrentItem().getItemMeta().getDisplayName());

                    if(reason != null) {
                        OfflinePlayer offlinePlayer = PanelManager.getPanel(player);

                        if(offlinePlayer != null) {
                            String command = reason.getCommand();

                            command = command.replaceAll("<player>", offlinePlayer.getName());

                            if(command.startsWith("/")) {
                                command = command.replaceFirst("/", "");
                            }

                            Bukkit.dispatchCommand(player, command);
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
                        }

                        player.closeInventory();
                    } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(nextPage)) {
                        int page = PanelManager.getPanelPage(player) + 1;
                        OfflinePlayer offlinePlayer = PanelManager.getPanel(player);

                        if(offlinePlayer != null) {
                            List<Ban> bans = BanManager.getPlayerBans(offlinePlayer.getUniqueId());

                            if(bans == null || bans.size() <= (7 * (page - 1))) {
                                player.playSound(player.getLocation(), Sound.BURP, 1f, 1f);
                                return;
                            }

                            player.playSound(player.getLocation(), Sound.CLICK, 1f, 1f);
                            InventoryBuilder.openBanInventory(player, PanelManager.getPanel(player), page);
                        }
                    } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(lastPage)) {
                        int page = PanelManager.getPanelPage(player) - 1;
                        OfflinePlayer offlinePlayer = PanelManager.getPanel(player);

                        if(offlinePlayer != null) {
                            List<Ban> bans = BanManager.getPlayerBans(offlinePlayer.getUniqueId());

                            if(bans == null || bans.size() <= (7 * (page - 1)) || page < 1) {
                                player.playSound(player.getLocation(), Sound.BURP, 1f, 1f);
                                return;
                            }

                            player.playSound(player.getLocation(), Sound.CLICK, 1f, 1f);
                            InventoryBuilder.openBanInventory(player, PanelManager.getPanel(player), page);
                        }
                    } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(viewMutes)) {
                        OfflinePlayer offlinePlayer = PanelManager.getPanel(player);

                        if(offlinePlayer != null) {
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.CLICK, 1f, 1f);
                            InventoryBuilder.openMuteInventory(player, offlinePlayer, 1);
                        }
                    } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(neverBanned) || event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(neverMuted)) {
                        player.playSound(player.getLocation(), Sound.BURP, 1f, 1f);
                    }
                }

            // MUTE INVENTORY
            } else if(event.getInventory().getName().equalsIgnoreCase(muteInventoryName)) {
                event.setCancelled(true);

                if(event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
                    Reason reason = Reason.fromItemName(event.getCurrentItem().getItemMeta().getDisplayName());

                    if(reason != null) {
                        OfflinePlayer offlinePlayer = PanelManager.getPanel(player);

                        if(offlinePlayer != null) {
                            String command = reason.getCommand();

                            command = command.replaceAll("<player>", offlinePlayer.getName());

                            if(command.startsWith("/")) {
                                command = command.replaceFirst("/", "");
                            }

                            Bukkit.dispatchCommand(player, command);
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
                        }

                        player.closeInventory();
                    } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(nextPage)) {
                        int page = PanelManager.getPanelPage(player) + 1;
                        OfflinePlayer offlinePlayer = PanelManager.getPanel(player);

                        if(offlinePlayer != null) {
                            List<Mute> mute = MuteManager.getPlayerMutes(offlinePlayer.getUniqueId());

                            if(mute == null || mute.size() <= (7 * (page - 1))) {
                                player.playSound(player.getLocation(), Sound.BURP, 1f, 1f);
                                return;
                            }

                            player.playSound(player.getLocation(), Sound.CLICK, 1f, 1f);
                            InventoryBuilder.openMuteInventory(player, PanelManager.getPanel(player), page);
                        }
                    } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(lastPage)) {
                        int page = PanelManager.getPanelPage(player) - 1;
                        OfflinePlayer offlinePlayer = PanelManager.getPanel(player);

                        if(offlinePlayer != null) {
                            List<Mute> mute = MuteManager.getPlayerMutes(offlinePlayer.getUniqueId());

                            if(mute == null || mute.size() <= (7 * (page - 1)) || page < 1) {
                                player.playSound(player.getLocation(), Sound.BURP, 1f, 1f);
                                return;
                            }

                            player.playSound(player.getLocation(), Sound.CLICK, 1f, 1f);
                            InventoryBuilder.openMuteInventory(player, PanelManager.getPanel(player), page);
                        }
                    } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(viewBans)) {
                        OfflinePlayer offlinePlayer = PanelManager.getPanel(player);

                        if(offlinePlayer != null) {
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.CLICK, 1f, 1f);
                            InventoryBuilder.openBanInventory(player, offlinePlayer, 1);
                        }
                    } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(neverBanned) || event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(neverMuted)) {
                        player.playSound(player.getLocation(), Sound.BURP, 1f, 1f);
                    }
                }
            }
        }
    }
}
