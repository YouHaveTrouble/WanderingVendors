package eu.endermite.wanderingvendors.gui;

import eu.endermite.wanderingvendors.WanderingVendors;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class TradeListSingle implements Listener {

    private static final NamespacedKey key = new NamespacedKey(WanderingVendors.getPlugin(), "guiitem");
    private static final NamespacedKey keyid = new NamespacedKey(WanderingVendors.getPlugin(), "guiitemid");
    private static final NamespacedKey vendor = new NamespacedKey(WanderingVendors.getPlugin(), "vendor");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = ((Player) e.getWhoClicked()).getPlayer();

            InventoryView inv = e.getWhoClicked().getOpenInventory();

            if (!inv.getTitle().startsWith("Edit merchant "))
                return;

            if (e.getClickedInventory() == null)
                return;

            e.setCancelled(true);

            if (e.getCurrentItem() == null)
                return;

            if (!e.getCurrentItem().hasItemMeta())
                return;

            String traderId = inv.getTitle().replace("Edit merchant ", "");
            Entity merchantEntity = Bukkit.getEntity(UUID.fromString(traderId));
            Merchant merchant = (Merchant) merchantEntity;

            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(keyid, PersistentDataType.STRING) != null) {
                String id = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(keyid, PersistentDataType.STRING);
                TradeCreator.openGui(p, traderId, id);
                return;
            }
            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING) != null) {
                switch (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING)) {
                    case ("add"):

                        TradeCreator.openGui(p, traderId, String.valueOf(merchant.getRecipeCount()));
                        break;
                    case ("next"):
                        int nextpage = e.getClickedInventory().getItem(49).getAmount();
                        TradeListMain.openGui(p, nextpage, WanderingVendors.getConfigCache().getMerchantTrades(), "Edit merchant", merchantEntity.getUniqueId().toString());
                        break;
                    case  ("previous"):
                        int prevpage = e.getClickedInventory().getItem(49).getAmount()-2;
                        TradeListMain.openGui(p, prevpage, WanderingVendors.getConfigCache().getMerchantTrades(), "Edit merchant", merchantEntity.getUniqueId().toString());
                        break;
                }
            }
        }
    }
}
