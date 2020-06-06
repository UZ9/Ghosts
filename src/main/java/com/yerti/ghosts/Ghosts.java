package com.yerti.ghosts;

import com.yerti.ghosts.commands.CommandGhost;
import com.yerti.ghosts.config.CommentYamlConfiguration;
import com.yerti.ghosts.config.ConfigManager;
import com.yerti.ghosts.core.commands.CustomCommand;
import com.yerti.ghosts.core.inventories.InventoryHandler;
import com.yerti.ghosts.event.EventTimer;
import com.yerti.ghosts.event.GhostPrevention;
import com.yerti.ghosts.gui.ItemInputInventory;
import com.yerti.ghosts.gui.RewardsGUI;
import com.yerti.ghosts.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Ghosts extends JavaPlugin {

    CommandGhost ghostCommand;
    ItemInputInventory editorInventory;
    EventTimer timer;

    @Override
    public void onEnable() {

        new ConfigManager(this).initConfig();


        //Add plugin instance to Utilities
        new Utilities(this);

        Utilities utilities = new Utilities();


        editorInventory = new ItemInputInventory();

        loadRewards();

        //Init commands
        ghostCommand = new CommandGhost("ghost", null);

        loadLocations();

        ghostCommand.initCommand();


        //Start event timer
        timer = new EventTimer(utilities.getEventInterval(), ghostCommand.getLocations(), this);
        timer.runTaskTimer(this, 0L, 20L);

        //Register events
        Bukkit.getPluginManager().registerEvents(timer, this);
        Bukkit.getPluginManager().registerEvents(new InventoryHandler(), this);
        Bukkit.getPluginManager().registerEvents(new ItemInputInventory(), this);
        Bukkit.getPluginManager().registerEvents(new RewardsGUI(), this);
        Bukkit.getPluginManager().registerEvents(new GhostPrevention(), this);


    }

    @Override
    public void onDisable() {

        reloadConfig();

        saveLocations();
        saveRewards();

        //getConfig().options().copyDefaults(true);

        saveConfig();

        timer.clean();

    }

    private void loadRewards() {

        if (getConfig().get("tiers") != null) {
            if (getConfig().getConfigurationSection("tiers") != null) {
                for (String key : getConfig().getConfigurationSection("tiers").getKeys(false)) {
                    String path = "tiers." + key;
                    List<ItemStack> items = (List<ItemStack>) getConfig().get(path + ".rewards");
                    editorInventory.getItems().put(Integer.parseInt(key), items);
                }
            }
        }
    }


    private void loadLocations() {
        Map<String, Location> locations = new HashMap<>();

        if (getConfig().get("locations") != null) {
            if (getConfig().getConfigurationSection("locations") != null) {
                for (String key : getConfig().getConfigurationSection("locations").getKeys(false)) {
                    String path = "locations." + key + ".";
                    double x = getConfig().getDouble(path + "x");
                    double y = getConfig().getDouble(path + "y");
                    double z = getConfig().getDouble(path + "z");
                    World world = Bukkit.getWorld(getConfig().getString(path + "world"));
                    locations.put(key, new Location(world, x, y, z));

                }
            }
        }

        ghostCommand.setLocations(locations);

    }


    private void saveLocations() {
        Map<String, Location> locations = ghostCommand.getLocations();



        for (Map.Entry<String, Location> entry : locations.entrySet()) {

            String path = "locations." + entry.getKey();
            Location location = entry.getValue();

            getConfig().set(path + ".x", location.getX());
            getConfig().set(path + ".y", location.getY());
            getConfig().set(path + ".z", location.getZ());
            getConfig().set(path + ".world", location.getWorld().getName());



        }

        getConfig().options().copyDefaults(true);
    }

    private void saveRewards() {
        Map<Integer, List<ItemStack>> items = editorInventory.getItems();

        for (Map.Entry<Integer, List<ItemStack>> entry : items.entrySet()) {

            String path = "tiers." + entry.getKey() + ".rewards";

            getConfig().set(path, entry.getValue());

        }

    }
}
