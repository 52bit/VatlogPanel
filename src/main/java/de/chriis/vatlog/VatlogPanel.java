package de.chriis.vatlog;

import de.chriis.vatlog.commands.PunishCommand;
import de.chriis.vatlog.listener.InventoryClickListener;
import de.chriis.vatlog.listener.InventoryCloseListener;
import de.chriis.vatlog.reasons.ReasonManager;
import de.chriis.vatlog.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class VatlogPanel extends JavaPlugin {

    private static VatlogPanel plugin;
    public static String prefix;
    public static String noPermission;

    @Override
    public void onEnable() {
        plugin = this;

        // Create Config
        ConfigManager.createCustomConfig();

        // Read Prefix and Messages from config
        prefix = getInstance().getConfig().getString("meta.prefix");
        noPermission = getInstance().getConfig().getString("meta.nopermission");

        // Read Reasons from Config
        ReasonManager.initialize();

        // Register Command
        getCommand("punish").setExecutor(new PunishCommand());

        // Register Events
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);

        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static VatlogPanel getInstance() {
        return plugin;
    }
}
