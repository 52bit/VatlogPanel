package de.chriis.vatlog.utils;

import de.chriis.vatlog.VatlogPanel;

import java.io.File;

public class ConfigManager {

    private static File configFile;

    public static void createCustomConfig() {
        configFile = new File(VatlogPanel.getInstance().getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            VatlogPanel.getInstance().saveResource("config.yml", false);
        }
    }
}