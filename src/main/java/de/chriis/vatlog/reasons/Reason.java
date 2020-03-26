package de.chriis.vatlog.reasons;


import org.bukkit.Material;

import java.util.List;

public class Reason {

    private String command;
    private List<String> lore;
    private Material material;
    private int slot;
    private String itemName;

    public Reason() {}

    public int getSlot() {
        return slot;
    }

    public Material getMaterial() {
        return material;
    }

    public String getCommand() {
        return command;
    }

    public String getItemName() {
        return itemName;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public static Reason fromItemName(String itemName) {
        for(Reason reason : ReasonManager.getReasons()) {
            if(reason.getItemName().equalsIgnoreCase(itemName)) {
                return reason;
            }
        }

        for(Reason reason : ReasonManager.getMuteReasons()) {
            if(reason.getItemName().equalsIgnoreCase(itemName)) {
                return reason;
            }
        }

        return null;
    }
}
