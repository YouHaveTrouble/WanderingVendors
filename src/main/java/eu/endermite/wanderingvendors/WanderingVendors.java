package eu.endermite.wanderingvendors;

import eu.endermite.wanderingvendors.commands.MainCommand;
import eu.endermite.wanderingvendors.config.ConfigCache;
import eu.endermite.wanderingvendors.config.CreatorTradesConfig;
import eu.endermite.wanderingvendors.gui.TradeCreator;
import eu.endermite.wanderingvendors.gui.TradeListMain;
import eu.endermite.wanderingvendors.gui.TradeListSingle;
import eu.endermite.wanderingvendors.listeners.VendorClickListener;
import eu.endermite.wanderingvendors.listeners.WanderingTraderSpawn;
import eu.endermite.wanderingvendors.npc.NPCManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class WanderingVendors extends JavaPlugin {

    private static WanderingVendors plugin;
    private static ConfigCache configCache;
    private static NPCManager npcManager;

    @Override
    public void onEnable() {
        plugin = this;
        reloadConfigData();

        Bukkit.getPluginManager().registerEvents(new WanderingTraderSpawn(), this);
        Bukkit.getPluginManager().registerEvents(new TradeCreator(), this);
        Bukkit.getPluginManager().registerEvents(new TradeListMain(), this);
        Bukkit.getPluginManager().registerEvents(new TradeListSingle(), this);
        Bukkit.getPluginManager().registerEvents(new VendorClickListener(), this);
        getCommand("wanderingvendors").setExecutor(new MainCommand());
        getCommand("wanderingvendors").setTabCompleter(new MainCommand());
    }

    public void reloadConfigData() {
        saveDefaultConfig();
        reloadConfig();

        configCache = new ConfigCache();
        npcManager = new NPCManager();
        CreatorTradesConfig.setupCreatorTrades();
    }

    public static WanderingVendors getPlugin() {return plugin;}
    public static ConfigCache getConfigCache() {return configCache;}
    public static NPCManager getNpcManager() {return npcManager;}

}

