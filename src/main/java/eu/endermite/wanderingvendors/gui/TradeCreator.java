package eu.endermite.wanderingvendors.gui;

import eu.endermite.wanderingvendors.WanderingVendors;
import eu.endermite.wanderingvendors.config.CreatorTradesConfig;
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
import java.util.UUID;

public class TradeCreator implements Listener {

    private static int tradeuses = 1;

    private static final NamespacedKey key = new NamespacedKey(WanderingVendors.getPlugin(), "guiitem");
    private static final NamespacedKey skey = new NamespacedKey(WanderingVendors.getPlugin(), "tradeid");

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
                case "delete":
                    String delid = e.getClickedInventory().getItem(1).getItemMeta().getPersistentDataContainer().get(skey, PersistentDataType.STRING);
                    if (delid != null) {
                        try {
                            WanderingVendors.getConfigCache().deleteTrade(delid);
                        } catch (NullPointerException ignored) {}
                        try {
                            CreatorTradesConfig.deleteTrade(delid);
                        } catch (NullPointerException ignored) {}
                    }
                    e.setCancelled(true);
                    p.closeInventory();
                    break;
                case "save":
                    ItemStack ing1 = e.getClickedInventory().getItem(3);
                    ItemStack ing2 = e.getClickedInventory().getItem(4);
                    ItemStack res = e.getClickedInventory().getItem(5);

                    ItemStack air = new ItemStack(Material.AIR, 1);

                    if (ing1 == null) {
                        ing1 = air;
                    }
                    if (ing2 == null) {
                        ing2 = air;
                    }
                    if (ing1 == air && ing2 == air) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTrade needs to have at least 1 ingridient!"));
                        e.setCancelled(true);
                        break;
                    }
                    if (res == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cResult cannot be empty!"));
                        e.setCancelled(true);
                        break;
                    } else {
                        MerchantRecipe recipe = new MerchantRecipe(res, tradeuses);
                        recipe.addIngredient(ing1);
                        recipe.addIngredient(ing2);
                        try {
                            String id = e.getClickedInventory().getItem(1).getItemMeta().getPersistentDataContainer().get(skey, PersistentDataType.STRING);
                            if (id == null) {
                                UUID uuid = UUID.randomUUID();
                                WanderingVendors.getConfigCache().addTrade(uuid.toString(), recipe);
                                CreatorTradesConfig.saveTrade(uuid.toString(), recipe);
                            } else {
                                WanderingVendors.getConfigCache().editTrade(id, recipe);
                                CreatorTradesConfig.saveTrade(id, recipe);
                            }
                        } catch (NullPointerException | IllegalArgumentException e1) {
                            e1.printStackTrace();
                        }
                        RefreshGuis.refresh();
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

        ItemStack instructioning = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
        ItemMeta instructionmeta = instructioning.getItemMeta();
        instructionmeta.setDisplayName(" ");
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

    public void openGui(Player player, String recipeID) {
        MerchantRecipe recipe = WanderingVendors.getConfigCache().getMerchantTrades().get(recipeID);
        this.tradeuses = recipe.getMaxUses();
        Inventory inv = Bukkit.createInventory(null, InventoryType.DROPPER, "Trade Creation");

        ItemStack block = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        ItemMeta blockmeta = block.getItemMeta();
        blockmeta.setDisplayName(" ");
        blockmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "block");
        block.setItemMeta(blockmeta);

        ItemStack instructioning = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
        ItemMeta instructionmeta = instructioning.getItemMeta();
        instructionmeta.setDisplayName(" ");
        instructionmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "block");
        instructionmeta.getPersistentDataContainer().set(skey, PersistentDataType.STRING, recipeID);
        instructioning.setItemMeta(instructionmeta);

        ItemStack delete = new ItemStack(Material.LAVA_BUCKET, 1);
        ItemMeta deletemeta = delete.getItemMeta();
        deletemeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lDelete Trade"));
        deletemeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "delete");
        delete.setItemMeta(deletemeta);

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
        inv.setItem(2, delete);
        inv.setItem(3, recipe.getIngredients().get(0));
        if (recipe.getIngredients().size() > 1) {
            inv.setItem(4, recipe.getIngredients().get(1));
        }
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
