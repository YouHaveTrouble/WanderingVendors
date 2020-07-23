package eu.endermite.wanderingvendors.gui;

import eu.endermite.wanderingvendors.WanderingVendors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
    private static final NamespacedKey keyid = new NamespacedKey(WanderingVendors.getPlugin(), "guiitemid");

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

            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(keyid, PersistentDataType.INTEGER) != null) {
                int id = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(keyid, PersistentDataType.INTEGER);
                TradeCreator creator = new TradeCreator();
                creator.openGui(p, id);
            }
            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING) != null) {
                switch (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING)) {
                    case ("next"):
                        int nextpage = e.getClickedInventory().getItem(49).getAmount();
                        openGui(p, nextpage);
                        break;
                    case  ("previous"):
                        int prevpage = e.getClickedInventory().getItem(49).getAmount()-2;
                        openGui(p, prevpage);
                        break;
                }
            }

        }
    }

    public void openGui(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, 54, "Trade List");

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

        inv.setItem(45, blue);
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

        for (int i = 0; i < 45; i++) {
            MerchantRecipe recipe = WanderingVendors.getConfigCache().getMerchantTrades().get(id);
            ItemStack item = recipe.getResult();
            ItemMeta itemmeta = item.getItemMeta();
            itemmeta.getPersistentDataContainer().set(keyid, PersistentDataType.INTEGER, id++);
            item.setItemMeta(itemmeta);
            inv.setItem(i, recipe.getResult());
            if (id >= WanderingVendors.getConfigCache().getMerchantTrades().size()) {
                break;
            }
        }
        player.openInventory(inv);

    }

}
