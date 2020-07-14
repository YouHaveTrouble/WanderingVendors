package eu.endermite.wanderingvendors;

import eu.endermite.wanderingvendors.commands.MainCommand;
import eu.endermite.wanderingvendors.config.ConfigCache;
import eu.endermite.wanderingvendors.listeners.WanderingTraderSpawn;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class WanderingVendors extends JavaPlugin {

    private static WanderingVendors plugin;
    private static ConfigCache configCache;

    @Override
    public void onEnable() {

        reloadConfigData();
        Bukkit.getPluginManager().registerEvents(new WanderingTraderSpawn(), this);
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

