package de.chriis.vatlog.listener;

import de.chriis.vatlog.utils.PanelManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if(PanelManager.getPanel(player) != null) {
            PanelManager.panelClosed(player);
        }
    }
}
