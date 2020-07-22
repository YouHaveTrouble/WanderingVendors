package eu.endermite.wanderingvendors.gui;

import eu.endermite.wanderingvendors.WanderingVendors;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class TradeList implements Listener {

    private static final NamespacedKey key = new NamespacedKey(WanderingVendors.getPlugin(), "guiitem");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = ((Player) e.getWhoClicked()).getPlayer();
            if (!e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase("Trade List"))
                return;

            if (e.getClickedInventory() == null)
                return;

            e.setCancelled(true);

            if (e.getCurrentItem() == null)
                return;

            if (!e.getCurrentItem().hasItemMeta())
                return;

            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER) != null) {
                int id = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                TradeCreator creator = new TradeCreator();
                creator.openGui(p, id);
            }

        }
    }

    public static void openGui(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, 54, "Trade List");

        int id = 44 * page;
        int slot = 0;

        for (MerchantRecipe recipe : WanderingVendors.getConfigCache().getMerchantTrades()) {

            if (slot >= 44) {
                break;
            }
            ItemStack item = recipe.getResult();
            ItemMeta itemmeta = item.getItemMeta();
            itemmeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, id++);
            item.setItemMeta(itemmeta);

            inv.setItem(slot++, recipe.getResult());
        }
        player.openInventory(inv);

    }

}
