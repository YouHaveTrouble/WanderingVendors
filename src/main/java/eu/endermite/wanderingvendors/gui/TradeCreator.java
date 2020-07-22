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
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class TradeCreator implements Listener {

    private static int tradeuses = 1;

    private static final NamespacedKey key = new NamespacedKey(WanderingVendors.getPlugin(), "guiitem");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = ((Player) e.getWhoClicked()).getPlayer();
            if (!e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase("Trade Creation"))
                return;

            if (e.getClickedInventory() == null)
                return;

            if (e.getCurrentItem() == null)
                return;

            if (!e.getCurrentItem().hasItemMeta())
                return;

            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING) == null) {
                return;
            }

            switch (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING)) {
                case "block":
                    e.setCancelled(true);
                    break;
                case "tradeuses":
                    if (e.getAction().equals(InventoryAction.PICKUP_ALL) && tradeuses < 64) {
                        this.tradeuses++;
                        e.getClickedInventory().setItem(e.getSlot(), getUsesDisplay(tradeuses));
                    } else if (e.getAction().equals(InventoryAction.PICKUP_HALF) && tradeuses > 1) {
                        this.tradeuses = tradeuses-1;
                        e.getClickedInventory().setItem(e.getSlot(), getUsesDisplay(tradeuses));
                    }
                    e.setCancelled(true);
                    break;
                case "save":
                    ItemStack ing1 = e.getClickedInventory().getItem(3);
                    ItemStack ing2 = e.getClickedInventory().getItem(4);
                    ItemStack res = e.getClickedInventory().getItem(5);

                    if (ing1 == null) {
                        ing1 = new ItemStack(Material.AIR, 1);
                    }
                    if (ing2 == null) {
                        ing2 = new ItemStack(Material.AIR, 1);
                    }
                    if (res == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cResult cannot be empty!"));
                    } else {
                        MerchantRecipe recipe = new MerchantRecipe(res, tradeuses);
                        recipe.addIngredient(ing1);
                        recipe.addIngredient(ing2);

                        try {
                            int id = e.getClickedInventory().getItem(1).getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                            WanderingVendors.getConfigCache().editTrade(id, recipe);
                        } catch (NullPointerException e1) {
                            WanderingVendors.getConfigCache().addTrade(recipe);
                        }

                        p.closeInventory();
                    }
                    e.setCancelled(true);
                    break;
                case "cancel":
                    e.setCancelled(true);
                    ItemStack cing1 = e.getClickedInventory().getItem(3);
                    ItemStack cing2 = e.getClickedInventory().getItem(4);
                    ItemStack cres = e.getClickedInventory().getItem(5);
                    p.closeInventory();
                    if (cing1 != null) {
                        p.getInventory().addItem(cing1);
                    }
                    if (cing2 != null) {
                        p.getInventory().addItem(cing2);
                    }
                    if (cres != null) {
                        p.getInventory().addItem(cres);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void openGui(Player player) {
        this.tradeuses = 1;
        Inventory inv = Bukkit.createInventory(null, InventoryType.DROPPER, "Trade Creation");

        ItemStack block = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        ItemMeta blockmeta = block.getItemMeta();
        blockmeta.setDisplayName(" ");
        blockmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "block");
        block.setItemMeta(blockmeta);

        ItemStack instructioning = new ItemStack(Material.PAPER, 1);
        ItemMeta instructionmeta = instructioning.getItemMeta();
        instructionmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&lInfo"));
        instructionmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "block");
        instructioning.setItemMeta(instructionmeta);

        ItemStack save = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta savemeta = save.getItemMeta();
        savemeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lSave Trade"));
        savemeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "save");
        save.setItemMeta(savemeta);

        ItemStack cancel = new ItemStack(Material.BARRIER, 1);
        ItemMeta cancelmeta = cancel.getItemMeta();
        cancelmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lCancel"));
        cancelmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "cancel");
        cancel.setItemMeta(cancelmeta);

        inv.setItem(0, getUsesDisplay(tradeuses));
        inv.setItem(1, instructioning);
        inv.setItem(2, block);
        inv.setItem(6, cancel);
        inv.setItem(7, block);
        inv.setItem(8, save);

        player.openInventory(inv);
    }

    public void openGui(Player player, Integer recipeID) {
        MerchantRecipe recipe = WanderingVendors.getConfigCache().getMerchantTrades().get(recipeID);
        this.tradeuses = recipe.getMaxUses();
        Inventory inv = Bukkit.createInventory(null, InventoryType.DROPPER, "Trade Creation");

        ItemStack block = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        ItemMeta blockmeta = block.getItemMeta();
        blockmeta.setDisplayName(" ");
        blockmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "block");
        block.setItemMeta(blockmeta);

        ItemStack instructioning = new ItemStack(Material.PAPER, 1);
        ItemMeta instructionmeta = instructioning.getItemMeta();
        instructionmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&lInfo"));
        instructionmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "block");
        instructionmeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, recipeID);
        instructioning.setItemMeta(instructionmeta);

        ItemStack save = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta savemeta = save.getItemMeta();
        savemeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lSave Trade"));
        savemeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "save");
        save.setItemMeta(savemeta);

        ItemStack cancel = new ItemStack(Material.BARRIER, 1);
        ItemMeta cancelmeta = cancel.getItemMeta();
        cancelmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lCancel"));
        cancelmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "cancel");
        cancel.setItemMeta(cancelmeta);

        inv.setItem(0, getUsesDisplay(tradeuses));
        inv.setItem(1, instructioning);
        inv.setItem(2, block);
        inv.setItem(3, recipe.getIngredients().get(0));
        inv.setItem(4, recipe.getIngredients().get(1));
        inv.setItem(5, recipe.getResult());
        inv.setItem(6, cancel);
        inv.setItem(7, block);
        inv.setItem(8, save);

        player.openInventory(inv);
    }

    public static ItemStack getUsesDisplay(int amount) {
        ItemStack tradeamount = new ItemStack(Material.STONE_BUTTON, amount);
        ItemMeta tradeamountmeta = tradeamount.getItemMeta();
        tradeamountmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&lTrade uses"));
        tradeamountmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "tradeuses");
        List<String> tamountlore = new ArrayList<String>();
        tamountlore.add("Uses: "+amount);
        tamountlore.add("LMB - add 1");
        tamountlore.add("RMB - remove 1");
        tradeamountmeta.setLore(tamountlore);
        tradeamount.setItemMeta(tradeamountmeta);
        return tradeamount;
    }
}
