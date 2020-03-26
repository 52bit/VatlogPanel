package de.chriis.vatlog.utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PanelManager {

    private static HashMap<Player, OfflinePlayer> panels = new HashMap<>();
    private static HashMap<Player, Integer> panelPage = new HashMap<>();

    public static OfflinePlayer getPanel(Player player) {
        return panels.get(player);
    }

    public static void setPanel(Player player, OfflinePlayer offlinePlayer) {
        panels.put(player, offlinePlayer);
    }

    public static void panelClosed(Player player) {
        panels.remove(player);
    }

    public static void setPanelPage(Player player, int page) {
        PanelManager.panelPage.put(player, page);
    }

    public static Integer getPanelPage(Player player) {
        if(panelPage.containsKey(player)) {
            return panelPage.get(player);
        }

        return 1;
    }
}
