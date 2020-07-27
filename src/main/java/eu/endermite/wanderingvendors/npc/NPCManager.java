package eu.endermite.wanderingvendors.npc;

import eu.endermite.wanderingvendors.WanderingVendors;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataType;
import java.io.File;
import java.util.*;

public class NPCManager {

    private NamespacedKey key = new NamespacedKey(WanderingVendors.getPlugin(), "WanderingVendorsNPC");
    private HashMap<String, WVNPC> npcList = new HashMap<>();

    private static final WanderingVendors plugin = WanderingVendors.getPlugin();
    private static FileConfiguration npcConfig;

    NPCManager() {
        File npcConfigFile = new File(plugin.getDataFolder(), "npc.yml");
        npcConfig = new YamlConfiguration();

        if (!npcConfigFile.exists()) {
            npcConfigFile.getParentFile().mkdirs();
            plugin.saveResource("npc.yml", false);
        }
        try {
            npcConfig.load(npcConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

            for (String id : npcConfig.getConfigurationSection("npc").getKeys(false)) {
                try {
                    npcList.put(id, loadNpc(id));
                } catch (NullPointerException ignored) {}
            }

    }

    public void createNPC(Location loc) {

        String id = UUID.randomUUID().toString();
        WVNPC npc = new WVNPC(id, loc, null);
        addNpc(id, npc);
        saveNPC(id);

    }

    public boolean saveNPC(String id) {
        try {
            WVNPC npc = npcList.get(id);
            if (npc.hasCustomName()) {
                npcConfig.set("npc."+id+".customname", npc.getCustomName());
            } else {
                npcConfig.set("npc."+id+".customname", null);
            }
            npcConfig.set("npc."+id+".location", npc.getLocation());

            if (!npc.getTradelist().isEmpty()) {
                for (Map.Entry<String, MerchantRecipe> set : npc.getTradelist().entrySet()) {
                    npcConfig.set("npc."+id+".trades."+set.getKey()+".uses", set.getValue().getMaxUses());
                    npcConfig.set("npc."+id+".trades."+set.getKey()+".result", set.getValue().getResult());
                    if (!set.getValue().getIngredients().isEmpty() && set.getValue().getIngredients().size() > 0) {
                        npcConfig.set("npc."+id+".trades."+set.getKey()+".ingridient1", set.getValue().getIngredients().get(0));
                        if (set.getValue().getIngredients().size() > 1) {
                            npcConfig.set("npc."+id+".trades."+set.getKey()+".ingridient2", set.getValue().getIngredients().get(1));
                        }
                    }
                }

            } else {
                npcConfig.set("npc."+id+".trades", null);
            }

        } catch (NullPointerException npe) {
            WanderingVendors.getPlugin().getLogger().severe("There was an errer while saving NPC "+id+" - required field not set");
            return false;
        }

        return true;
    }

    public void spawnNpc(String id, Location loc) {

        WVNPC npc = npcList.get(id);

        WanderingTrader tradernpc = (WanderingTrader) loc.getWorld().spawnEntity(loc, EntityType.WANDERING_TRADER);
        tradernpc.setGravity(false);
        tradernpc.setInvulnerable(true);
        tradernpc.setAI(false);
        tradernpc.getPersistentDataContainer().set(key, PersistentDataType.STRING, id);
        tradernpc.setAgeLock(true);

        List<MerchantRecipe> recipeList = new ArrayList<>();
        if (!npc.getTradelist().isEmpty()) {
            for (MerchantRecipe recipe : npc.getTradelist().values()) {
                recipeList.add(recipe);
            }
        }

        tradernpc.setRecipes(recipeList);
        if (npc.hasCustomName()) {
            tradernpc.setCustomNameVisible(true);
            tradernpc.setCustomName(npc.getCustomName());
        }

    }

    public WVNPC loadNpc(String id) {
        Location loc = npcConfig.getLocation("npc."+id+".location");
        String customName = npcConfig.getString("npc."+id+".customname");
        HashMap<String, MerchantRecipe> trades = new HashMap<>();

        ConfigurationSection tradeSection = npcConfig.getConfigurationSection("npc."+id+".trades");
        for (String pointer : tradeSection.getKeys(false)) {
            try {
                ItemStack result = npcConfig.getItemStack("npc."+id+".trades."+pointer+".result");
                int uses = npcConfig.getInt("npc."+id+".trades."+pointer+".uses");
                MerchantRecipe recipe = new MerchantRecipe(result, uses);
                ItemStack ing1 = npcConfig.getItemStack("npc."+id+".trades."+pointer+".ingridient1");
                ItemStack ing2 = npcConfig.getItemStack("npc."+id+".trades."+pointer+".ingridient2");

                if (ing1 != null) {
                    recipe.addIngredient(ing1);
                }
                if (ing2 != null) {
                    recipe.addIngredient(ing2);
                }
                if (recipe.getIngredients().size() == 0) {
                    WanderingVendors.getPlugin().getLogger().severe("Failed to load trade "+pointer+" for NPC "+id);
                    continue;
                }
                trades.put(pointer, recipe);

            } catch (NullPointerException e) {
                WanderingVendors.getPlugin().getLogger().severe("Failed to load trade "+pointer+" for NPC "+id);
            }

        }
        WVNPC createdNpc = new WVNPC(id, loc, trades);
        if (customName != null) {
            createdNpc.setCustomName(customName);
        }
        return createdNpc;

    }

    public HashMap<String, WVNPC> getNpcList() {
        return npcList;
    }

    public void addNpc(String id, WVNPC npc) {
        npcList.put(id, npc);
    }

    public void removeNpc(String id) {
        npcList.remove(id);
    }


}
