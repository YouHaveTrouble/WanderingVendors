package eu.endermite.wanderingvendors.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public class RefreshGuis {

    public static void refresh() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            try {
                InventoryView inv = p.getOpenInventory();
            } catch (NullPointerException ignored) {
                continue;
            }
            if (p.getOpenInventory().getTitle().equals("Trade List")) {
                try {
                    int page = p.getOpenInventory().getItem(49).getAmount();
                    page = page-1;
                    TradeList tradelist = new TradeList();
                    tradelist.openGui(p, page);
                } catch (Exception ignored) {}
            }




        }
    }

}
