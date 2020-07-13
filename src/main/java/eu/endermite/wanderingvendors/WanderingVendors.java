package eu.endermite.wanderingvendors;

import eu.endermite.wanderingvendors.config.ConfigCache;
import eu.endermite.wanderingvendors.listeners.WanderingTraderSpawn;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class WanderingVendors extends JavaPlugin {

    private static WanderingVendors plugin;
    private static ConfigCache configCache;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;
        configCache = new ConfigCache();
        Bukkit.getPluginManager().registerEvents(new WanderingTraderSpawn(), this);

    }


    public static WanderingVendors getPlugin() {return plugin;}
    public static ConfigCache getConfigCache() {return configCache;}

}

