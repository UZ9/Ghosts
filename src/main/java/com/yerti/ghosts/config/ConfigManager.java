package com.yerti.ghosts.config;


import org.bukkit.plugin.Plugin;

import java.io.File;

public class ConfigManager {

    Plugin plugin;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void initConfig() {
        File file = new File(plugin.getDataFolder(), "config.yml");
        if(!file.exists()){
            plugin.saveDefaultConfig();
        }
    }

}
