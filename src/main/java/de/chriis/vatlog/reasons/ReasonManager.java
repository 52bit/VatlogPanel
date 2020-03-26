package de.chriis.vatlog.reasons;

import de.chriis.vatlog.VatlogPanel;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ReasonManager {

    private static ArrayList<Reason> reasons = new ArrayList<>();
    private static ArrayList<Reason> muteReasons = new ArrayList<>();

    public static void initialize() {
        // Fetch Ban Reasons
        MemorySection reasons = (MemorySection) VatlogPanel.getInstance().getConfig().get("banreasons");
        Set<String> reasonNames = reasons.getKeys(false);

        for(String reasonName : reasonNames) {
            Reason reason = new Reason();

            String command = VatlogPanel.getInstance().getConfig().getString("banreasons." + reasonName + ".command");
            List<String> lore = VatlogPanel.getInstance().getConfig().getStringList("banreasons." + reasonName + ".lore");
            String materialString = VatlogPanel.getInstance().getConfig().getString("banreasons." + reasonName + ".material");
            Material material = Material.valueOf(materialString);
            int slot = VatlogPanel.getInstance().getConfig().getInt("banreasons." + reasonName + ".slot");
            String itemName = VatlogPanel.getInstance().getConfig().getString("banreasons." + reasonName + ".itemName");

            reason.setCommand(command);
            reason.setSlot(slot);
            reason.setItemName(itemName);
            reason.setLore(lore);
            reason.setMaterial(material);

            ReasonManager.getReasons().add(reason);
        }

        // Fetch Mute Reasons
        MemorySection muteReasons = (MemorySection) VatlogPanel.getInstance().getConfig().get("mutereasons");
        Set<String> muteReasonNames = muteReasons.getKeys(false);

        for(String reasonName : muteReasonNames) {
            Reason reason = new Reason();

            String command = VatlogPanel.getInstance().getConfig().getString("mutereasons." + reasonName + ".command");
            List<String> lore = VatlogPanel.getInstance().getConfig().getStringList("mutereasons." + reasonName + ".lore");
            String materialString = VatlogPanel.getInstance().getConfig().getString("mutereasons." + reasonName + ".material");
            Material material = Material.valueOf(materialString);
            int slot = VatlogPanel.getInstance().getConfig().getInt("mutereasons." + reasonName + ".slot");
            String itemName = VatlogPanel.getInstance().getConfig().getString("mutereasons." + reasonName + ".itemName");

            reason.setCommand(command);
            reason.setSlot(slot);
            reason.setItemName(itemName);
            reason.setLore(lore);
            reason.setMaterial(material);

            ReasonManager.getMuteReasons().add(reason);
        }
    }

    public static ArrayList<Reason> getReasons() {
        return reasons;
    }

    public static ArrayList<Reason> getMuteReasons() {
        return muteReasons;
    }
}
