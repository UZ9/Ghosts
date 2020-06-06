package com.yerti.ghosts.event;

import com.yerti.ghosts.GhostsAPI;
import com.yerti.ghosts.utils.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GhostPrevention implements Listener {

    private Map<UUID, Double> cooldown = new HashMap<>();
    private Utilities utilities = new Utilities();

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event) {
        if (GhostsAPI.getGhostEntity() == null) return;

        if (cooldown.containsKey(event.getPlayer().getUniqueId())) {
            if (cooldown.get(event.getPlayer().getUniqueId()) + 800 - System.currentTimeMillis() > 0) {
                return;
            }

        }

        if (event.getPlayer().getLocation().getWorld().equals(GhostsAPI.getGhostEntity().getWorld())) {
            if (event.getPlayer().getLocation().distanceSquared(GhostsAPI.getGhostEntity().getLocation()) < 2.) {
                event.getPlayer().sendMessage(utilities.getPrefix() + ChatColor.RED + " You can't stand inside the Ghost!");

                event.getPlayer().setVelocity(event.getPlayer().getLocation().toVector().subtract(GhostsAPI.getGhostEntity().getLocation().toVector()).normalize().multiply(4));
                cooldown.put(event.getPlayer().getUniqueId(), (double) System.currentTimeMillis());
            }
        }
    }


}
