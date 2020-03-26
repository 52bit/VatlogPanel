package de.chriis.vatlog.utils;

import de.chriis.vatlog.VatlogPanel;
import de.chriis.vatlog.bans.Ban;
import de.chriis.vatlog.bans.BanManager;
import de.chriis.vatlog.mutes.Mute;
import de.chriis.vatlog.mutes.MuteManager;
import de.chriis.vatlog.reasons.Reason;
import de.chriis.vatlog.reasons.ReasonManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

public class InventoryBuilder {

    private static int slot;

    private static String lastPage = VatlogPanel.getInstance().getConfig().getString("meta.lastPage");
    private static String nextPage = VatlogPanel.getInstance().getConfig().getString("meta.nextPage");
    private static String viewBans = VatlogPanel.getInstance().getConfig().getString("meta.viewBans");
    private static String viewMutes = VatlogPanel.getInstance().getConfig().getString("meta.viewMutes");
    private static String neverBanned = VatlogPanel.getInstance().getConfig().getString("meta.neverBanned");
    private static String neverMuted = VatlogPanel.getInstance().getConfig().getString("meta.neverMuted");

    public static void openBanInventory(Player player, OfflinePlayer offlinePlayer, int page) {
        PanelManager.setPanelPage(player, page);

        BanManager.fetchPlayerBans(offlinePlayer.getUniqueId(), () -> {
            // Get inventory title from config
            String inventoryName = VatlogPanel.getInstance().getConfig().getString("inventory.bantitle");

            PanelManager.setPanel(player, offlinePlayer);

            // Open Vatlog Panel
            Executors.newCachedThreadPool().execute(() -> {
                if(player.getOpenInventory() != null && player.getOpenInventory().getTitle().equalsIgnoreCase(inventoryName)) {
                    //Update Inventory
                    for(int i = 46; i < 53; i++) {
                        player.getOpenInventory().setItem(i, null);
                    }

                    slot = 46;

                    List<Ban> bans = BanManager.getPlayerBans(offlinePlayer.getUniqueId());

                    int start = 7 * (page - 1);
                    int end = (page * 7);

                    if (start > bans.size()) {
                        // Error
                    }

                    if (end > bans.size()) {
                        end = bans.size();
                    }

                    for (Ban ban : bans.subList(start, end)) {
                        String timeFormatted = TimeFormatter.getFormattedDate(ban.getTime());
                        String untilFormatted = TimeFormatter.formatUntil(ban.getUntil() - System.currentTimeMillis());
                        String fFormatted = TimeFormatter.formatUntil(ban.getUntil() - ban.getTime());
                        int subId = ban.isActive() ? 13 : 14;

                        OfflinePlayer offlinePlayerBy = Bukkit.getOfflinePlayer(UUID.fromString(ban.getBannedByUuid()));

                        player.getOpenInventory().setItem(slot, new ItemBuilder(Material.STAINED_CLAY, 1, subId)
                                .withName("§5" + timeFormatted).withLores(
                                        Arrays.asList("§8--------------------------------",
                                                "§8ID: §7" + ban.getId(),
                                                "§8By: §7" + offlinePlayerBy.getName(),
                                                "§8Server: §7" + ban.getServer(),
                                                "§8Reason: §7" + ban.getReason(),
                                                "§8Expires In: §7" + (ban.isActive() ? untilFormatted + " (" + fFormatted + ")" : "Expired"),
                                                "§8--------------------------------")
                                ).toItemStack());
                        slot++;
                    }

                    player.updateInventory();
                } else {
                    Inventory inventory = Bukkit.createInventory(null, 9 * 6, inventoryName);

                    inventory.setItem(4, new ItemBuilder(Material.SKULL_ITEM, 1).toPlayerHead(offlinePlayer.getName()));

                    // Mutes Item
                    inventory.setItem(8, new ItemBuilder(Material.REDSTONE, 1).withName(viewMutes).toItemStack());

                    // Ban Reasons
                    for (Reason reason : ReasonManager.getReasons()) {
                        List<String> loreReplaced = new ArrayList<>();

                        reason.getLore().forEach(line -> {
                            loreReplaced.add(line.replace("<player>", offlinePlayer.getName()));
                        });

                        inventory.setItem(reason.getSlot(), new ItemBuilder(reason.getMaterial(), 1, 0).withName(reason.getItemName()).withLores(loreReplaced).toItemStack());
                    }

                    // Set Slot to 46 (for prev bans)
                    slot = 46;

                    List<Ban> bans = BanManager.getPlayerBans(offlinePlayer.getUniqueId());

                    if(bans == null) {
                        return;
                    }

                    // Books
                    if(bans.size() > 7) {
                        inventory.setItem(45, new ItemBuilder(Material.BOOK, 1).withName(lastPage).toItemStack());
                        inventory.setItem(53, new ItemBuilder(Material.BOOK, 1).withName(nextPage).toItemStack());
                    }

                    if(bans.size() == 0) {
                        inventory.setItem(49, new ItemBuilder(Material.BARRIER, 1).withName(neverBanned).toItemStack());
                    } else {
                        int start = 8 * (page - 1);
                        int end = (page * 8) - 1;

                        if (start > bans.size()) {
                            // Error
                        }

                        if (end > bans.size()) {
                            end = bans.size();
                        }

                        for (Ban ban : bans.subList(start, end)) {
                            String timeFormatted = TimeFormatter.getFormattedDate(ban.getTime());
                            String untilFormatted = TimeFormatter.formatUntil(ban.getUntil() - System.currentTimeMillis());
                            String fFormatted = TimeFormatter.formatUntil(ban.getUntil() - ban.getTime());
                            int subId = ban.isActive() ? 13 : 14;

                            OfflinePlayer offlinePlayerBy = Bukkit.getOfflinePlayer(UUID.fromString(ban.getBannedByUuid()));

                            inventory.setItem(slot, new ItemBuilder(Material.STAINED_CLAY, 1, subId)
                                    .withName("§5" + timeFormatted).withLores(
                                            Arrays.asList("§8--------------------------------",
                                                    "§8ID: §7" + ban.getId(),
                                                    "§8By: §7" + offlinePlayerBy.getName(),
                                                    "§8Server: §7" + ban.getServer(),
                                                    "§8Reason: §7" + ban.getReason(),
                                                    "§8Expires In: §7" + (ban.isActive() ? untilFormatted + " (" + fFormatted + ")" : "Expired"),
                                                    "§8--------------------------------")
                                    ).toItemStack());
                            slot++;
                        }
                    }

                    player.openInventory(inventory);
                }
            });
        });
    }

    public static void openMuteInventory(Player player, OfflinePlayer offlinePlayer, int page) {
        PanelManager.setPanelPage(player, page);

        MuteManager.fetchPlayerMutes(offlinePlayer.getUniqueId(), () -> {
            // Get inventory title from config
            String inventoryName = VatlogPanel.getInstance().getConfig().getString("inventory.mutetitle");

            PanelManager.setPanel(player, offlinePlayer);

            // Open Vatlog Panel
            Executors.newCachedThreadPool().execute(() -> {
                if(player.getOpenInventory() != null && player.getOpenInventory().getTitle().equalsIgnoreCase(inventoryName)) {
                    //Update Inventory
                    for(int i = 46; i < 53; i++) {
                        player.getOpenInventory().setItem(i, null);
                    }

                    slot = 46;

                    List<Mute> mutes = MuteManager.getPlayerMutes(offlinePlayer.getUniqueId());

                    if(mutes.size() == 0) {
                        player.getOpenInventory().setItem(49, new ItemBuilder(Material.BARRIER, 1).withName(neverMuted).toItemStack());
                    } else {
                        int start = 7 * (page - 1);
                        int end = (page * 7);

                        if (start > mutes.size()) {
                            // Error
                        }

                        if (end > mutes.size()) {
                            end = mutes.size();
                        }

                        for (Mute mute : mutes.subList(start, end)) {
                            String timeFormatted = TimeFormatter.getFormattedDate(mute.getTime());
                            String untilFormatted = TimeFormatter.formatUntil(mute.getUntil() - System.currentTimeMillis());
                            String fFormatted = TimeFormatter.formatUntil(mute.getUntil() - mute.getTime());
                            int subId = mute.isActive() ? 13 : 14;

                            OfflinePlayer offlinePlayerBy = Bukkit.getOfflinePlayer(UUID.fromString(mute.getBannedByUuid()));

                            player.getOpenInventory().setItem(slot, new ItemBuilder(Material.STAINED_CLAY, 1, subId)
                                    .withName("§5" + timeFormatted).withLores(
                                            Arrays.asList("§8--------------------------------",
                                                    "§8ID: §7" + mute.getId(),
                                                    "§8By: §7" + offlinePlayerBy.getName(),
                                                    "§8Server: §7" + mute.getServer(),
                                                    "§8Reason: §7" + mute.getReason(),
                                                    "§8Expires In: §7" + (mute.isActive() ? untilFormatted + " (" + fFormatted + ")" : "Expired"),
                                                    "§8--------------------------------")
                                    ).toItemStack());
                            slot++;
                        }
                    }

                    player.updateInventory();
                } else {
                    Inventory inventory = Bukkit.createInventory(null, 9 * 6, inventoryName);

                    inventory.setItem(4, new ItemBuilder(Material.SKULL_ITEM, 1).toPlayerHead(offlinePlayer.getName()));

                    // Mutes Item
                    inventory.setItem(8, new ItemBuilder(Material.REDSTONE, 1).withName(viewBans).toItemStack());

                    // Mute Reasons
                    for (Reason reason : ReasonManager.getMuteReasons()) {
                        List<String> loreReplaced = new ArrayList<>();

                        reason.getLore().forEach(line -> {
                            loreReplaced.add(line.replace("<player>", offlinePlayer.getName()));
                        });

                        inventory.setItem(reason.getSlot(), new ItemBuilder(reason.getMaterial(), 1, 0).withName(reason.getItemName()).withLores(loreReplaced).toItemStack());
                    }

                    // Set Slot to 46 (for prev bans)
                    slot = 46;

                    List<Mute> mutes = MuteManager.getPlayerMutes(offlinePlayer.getUniqueId());

                    if(mutes == null) {
                        return;
                    }

                    if(mutes.size() > 7) {
                        // Books
                        inventory.setItem(45, new ItemBuilder(Material.BOOK, 1).withName(lastPage).toItemStack());
                        inventory.setItem(53, new ItemBuilder(Material.BOOK, 1).withName(nextPage).toItemStack());
                    }

                    if(mutes.size() == 0) {
                        inventory.setItem(49, new ItemBuilder(Material.BARRIER, 1).withName(neverMuted).toItemStack());
                    } else {
                        int start = 8 * (page - 1);
                        int end = (page * 8) - 1;

                        if (start > mutes.size()) {
                            // Error
                        }

                        if (end > mutes.size()) {
                            end = mutes.size();
                        }

                        for (Mute mute : mutes.subList(start, end)) {
                            String timeFormatted = TimeFormatter.getFormattedDate(mute.getTime());
                            String untilFormatted = TimeFormatter.formatUntil(mute.getUntil() - System.currentTimeMillis());
                            String fFormatted = TimeFormatter.formatUntil(mute.getUntil() - mute.getTime());
                            int subId = mute.isActive() ? 13 : 14;

                            OfflinePlayer offlinePlayerBy = Bukkit.getOfflinePlayer(UUID.fromString(mute.getBannedByUuid()));

                            inventory.setItem(slot, new ItemBuilder(Material.STAINED_CLAY, 1, subId)
                                    .withName("§5" + timeFormatted).withLores(
                                            Arrays.asList("§8--------------------------------",
                                                    "§8ID: §7" + mute.getId(),
                                                    "§8By: §7" + offlinePlayerBy.getName(),
                                                    "§8Server: §7" + mute.getServer(),
                                                    "§8Reason: §7" + mute.getReason(),
                                                    "§8Expires In: §7" + (mute.isActive() ? untilFormatted + " (" + fFormatted + ")" : "Expired"),
                                                    "§8--------------------------------")
                                    ).toItemStack());
                            slot++;
                        }
                    }

                    player.openInventory(inventory);
                }
            });
        });
    }
}
