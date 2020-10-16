package eu.endermite.wanderingvendors.gui;

import eu.endermite.wanderingvendors.WanderingVendors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
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
import java.util.HashMap;
import java.util.Map;

public class TradeListMain implements Listener {

    private static final NamespacedKey key = new NamespacedKey(WanderingVendors.getPlugin(), "guiitem");
    private static final NamespacedKey keyid = new NamespacedKey(WanderingVendors.getPlugin(), "guiitemid");
    private static final NamespacedKey vendor = new NamespacedKey(WanderingVendors.getPlugin(), "vendor");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = ((Player) e.getWhoClicked()).getPlayer();
            if (!e.getWhoClicked().getOpenInventory().getTitle().equals("Trade List"))
                return;

            if (e.getClickedInventory() == null)
                return;

            e.setCancelled(true);

            if (e.getCurrentItem() == null)
                return;

            if (!e.getCurrentItem().hasItemMeta())
                return;

            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(keyid, PersistentDataType.STRING) != null) {
                String id = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(keyid, PersistentDataType.STRING);
                TradeCreator.openGui(p, null, id);
            }
            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING) != null) {
                switch (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING)) {
                    case ("next"):
                        int nextpage = e.getClickedInventory().getItem(49).getAmount();
                        openGui(p, nextpage, WanderingVendors.getConfigCache().getMerchantTrades(), "Trade List", null);
                        break;
                    case  ("previous"):
                        int prevpage = e.getClickedInventory().getItem(49).getAmount()-2;
                        openGui(p, prevpage, WanderingVendors.getConfigCache().getMerchantTrades(), "Trade List", null);
                        break;
                }
            }

        }
    }

    public static void openGui(Player player, int page, HashMap<String, MerchantRecipe> cache, String title, String traderEntity) {

        Inventory inv = Bukkit.createInventory(null, 54, title);

        int id = 44 * page;
        int pagenum = page+1;

        ItemStack blue = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        ItemMeta bluemeta = blue.getItemMeta();
        bluemeta.setDisplayName(" ");
        bluemeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "block");
        blue.setItemMeta(bluemeta);

        ItemStack gold = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
        ItemMeta goldmeta = gold.getItemMeta();
        goldmeta.setDisplayName(" ");
        goldmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "block");
        gold.setItemMeta(goldmeta);

        ItemStack add = new ItemStack(Material.CHORUS_PLANT, 1);
        ItemMeta addmeta = add.getItemMeta();
        addmeta.setDisplayName(" ");
        addmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "add");
        if (traderEntity != null) {
            addmeta.getPersistentDataContainer().set(vendor, PersistentDataType.STRING, traderEntity);
        }
        add.setItemMeta(addmeta);

        ItemStack previous = new ItemStack(Material.PAPER, 1);
        ItemMeta previousmeta = previous.getItemMeta();
        previousmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&lPrevious Page") );
        previousmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "previous");
        previous.setItemMeta(previousmeta);

        ItemStack next = new ItemStack(Material.PAPER, 1);
        ItemMeta nextmeta = previous.getItemMeta();
        nextmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&lNext Page"));
        nextmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "next");
        next.setItemMeta(nextmeta);

        ItemStack current = new ItemStack(Material.BOOK, pagenum);
        ItemMeta currentmeta = previous.getItemMeta();
        currentmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&lCurrent Page: "+pagenum));
        currentmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "block");
        current.setItemMeta(currentmeta);

        if (traderEntity != null) {
            inv.setItem(45, add);
        } else {
            inv.setItem(45, blue);
        }
        inv.setItem(46, blue);
        inv.setItem(47, gold);
        inv.setItem(49, current);
        inv.setItem(51, gold);
        inv.setItem(52, blue);
        inv.setItem(53, blue);

        if (page > 0) {
            inv.setItem(48, previous);
        } else {
            inv.setItem(48, blue);
        }


        if (WanderingVendors.getConfigCache().getMerchantTrades().size() > 44*pagenum) {
            inv.setItem(50, next);
        } else {
            inv.setItem(50, blue);
        }

        if (page != 0) {
            int iter = 0;
            for (Map.Entry<String, MerchantRecipe> entry : cache.entrySet()) {

                if (iter < id || iter >= 45) {
                    break;
                }
                inv.setItem(iter, entry.getValue().getResult());
                iter++;
            }
        } else {
            int iter = 0;
            for (Map.Entry<String, MerchantRecipe> entry : cache.entrySet()) {

                if (iter >= 45) {
                    break;
                }
                ItemStack item = entry.getValue().getResult().clone();
                ItemMeta meta = item.getItemMeta();
                meta.getPersistentDataContainer().set(keyid, PersistentDataType.STRING, entry.getKey());
                item.setItemMeta(meta);
                inv.setItem(iter, item);
                iter++;
            }
        }
        player.openInventory(inv);
    }

}
