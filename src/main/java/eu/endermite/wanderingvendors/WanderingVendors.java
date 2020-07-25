package eu.endermite.wanderingvendors;

import eu.endermite.wanderingvendors.commands.MainCommand;
import eu.endermite.wanderingvendors.config.ConfigCache;
import eu.endermite.wanderingvendors.config.CreatorTradesConfig;
import eu.endermite.wanderingvendors.gui.TradeCreator;
import eu.endermite.wanderingvendors.gui.TradeList;
import eu.endermite.wanderingvendors.listeners.WanderingTraderSpawn;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class WanderingVendors extends JavaPlugin {

    private static WanderingVendors plugin;
    private static ConfigCache configCache;

    @Override
    public void onEnable() {


        reloadConfigData();
        CreatorTradesConfig.setupCreatorTrades();
        Bukkit.getPluginManager().registerEvents(new WanderingTraderSpawn(), this);
        Bukkit.getPluginManager().registerEvents(new TradeCreator(), this);
        Bukkit.getPluginManager().registerEvents(new TradeList(), this);
        getCommand("wanderingvendors").setExecutor(new MainCommand());
    }

    public void reloadConfigData() {
        saveDefaultConfig();
        reloadConfig();
        plugin = this;
        configCache = new ConfigCache();
    }

    public static WanderingVendors getPlugin() {return plugin;}
    public static ConfigCache getConfigCache() {return configCache;}

}

