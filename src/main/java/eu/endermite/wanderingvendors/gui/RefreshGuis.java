package eu.endermite.wanderingvendors.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public class RefreshGuis {

    public void refresh() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            try {
                InventoryView inv = p.getOpenInventory();
            } catch (NullPointerException ignored) {
                continue;
            }
            if (p.getOpenInventory().getTitle().equals("Trade List")) {
                p.closeInventory();
            }




        }
    }

}
