package com.yerti.ghosts.event;


import com.yerti.ghosts.Ghosts;
import com.yerti.ghosts.config.CommentYamlConfiguration;
import com.yerti.ghosts.gui.ItemInputInventory;
import com.yerti.ghosts.gui.RewardsGUI;
import com.yerti.ghosts.utils.Utilities;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.rmi.CORBA.Util;
import java.util.*;
import java.util.stream.Collectors;


public class EventTimer extends BukkitRunnable implements Listener {


    //TODO: Static is a temporary solution to /forcespawn, working on redoing it now
    private static int delay;
    private static int currentTime;
    private static Map<String, Location> locations;
    private static List<Player> attackers;
    static int tier;
    private static Plugin plugin;

    static boolean eventSpawned = false;

    static Utilities utilities;
    static LivingEntity eventEntity;

    public EventTimer(int delay, Map<String, Location> locations, Plugin plugin) {
        this.delay = delay;
        this.currentTime = delay;
        this.locations = locations;
        this.attackers = new ArrayList<Player>();
        this.plugin = plugin;
        this.utilities = new Utilities();
    }



    //TODO: Use reflection and move to utilities
    private static void noAI(Entity entity) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        NBTTagCompound tagCompound = nmsEntity.getNBTTag();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
        }
        nmsEntity.c(tagCompound);
        tagCompound.setInt("NoAI", 1);
        nmsEntity.f(tagCompound);
    }

    public static void updateHealthName(LivingEntity entity) {
        entity.setCustomNameVisible(true);
        entity.setCustomName("" + ChatColor.RED + ((int) entity.getHealth()) +  " \u2764");
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (eventEntity == null) return;
        if (event.getEntity() != null) {
            if (event.getEntity() == eventEntity) {
                event.setDamage(event.getDamage() * (1024 / utilities.getMaxHealth(tier)));
                updateHealthName((LivingEntity) event.getEntity());


                if (event.getDamager() != null) {
                    if (event.getDamager() instanceof Player) {
                        if (!attackers.contains(attackers)) {
                            attackers.add((Player) event.getDamager());
                        }
                    }

                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {


        if (eventEntity == null) return;

        if (event.getEntity().equals(eventEntity)) {
            event.getDrops().clear();
            Bukkit.broadcastMessage(utilities.eventDeathMessage(tier));

            for (Player player : attackers) {

                if (player.getOpenInventory() != null) player.closeInventory();
                Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(Ghosts.class), () -> {
                    player.openInventory(new RewardsGUI().getInventory(tier));
                }, 20L);

            }


            eventSpawned = false;
            eventEntity = null;
            attackers.clear();
        }


    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {

        if (eventEntity == null) return;

        if (event.getEntity().equals(eventEntity)) {
            event.setCancelled(true);
        }
    }

    //TODO: Redo this to be not static
    public static void spawnGhost() {
        if (eventEntity != null) {
            eventEntity.remove();
            eventSpawned = false;
            attackers.clear();
            eventEntity = null;
        } else {
            attackers.clear();
            eventSpawned = false;
        }


        currentTime = delay;

        if (locations.size() == 0) return;

        //Select from locations
        Location[] locArray = locations.values().toArray(new Location[locations.size()]);

        int index = Utilities.getRandomNumberInRange(0, locArray.length - 1);

        locArray[index].getWorld().loadChunk(locArray[index].getChunk());

        eventEntity = (LivingEntity) locArray[index].getWorld().spawnEntity(locArray[index], utilities.getEntityType());




        //ItemInputInventory tierManager = new ItemInputInventory();

        tier = Utilities.getRandomNumberInRange(1, 3);

        eventEntity.setMaxHealth(1024);
        eventEntity.setHealth(1024);

        noAI(eventEntity);
        updateHealthName(eventEntity);
        eventSpawned = true;

        Bukkit.broadcastMessage(utilities.eventMobSpawnMessage().replace("[tier]", String.valueOf(tier)).replace("[locationname]", getKeysByValue(locations, locArray[index])));

    }


    @Override
    public void run() {


        if (currentTime <= 0) {
            currentTime = delay;

            spawnGhost();


        } else {


            if (eventSpawned) return;
            if (locations.size() == 0) return;

            currentTime--;
        }
    }

    private static <T, E> T getKeysByValue(Map<T, E> map,  E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(entry.getValue(), value)) {
                return entry.getKey();
            }
        }


        return null;
    }

    public void clean() {
        if (eventEntity != null) {
            eventEntity.remove();
        }
    }



}