package com.yerti.ghosts.core.block;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockUtils {

    public static void breakBlock(Player p, Block b) {
        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(b, p);
        Bukkit.getServer().getPluginManager().callEvent(blockBreakEvent);
        if (!blockBreakEvent.isCancelled()) {
            blockBreakEvent.setCancelled(true);
            for (ItemStack drop : b.getDrops()) {
                b.getWorld().dropItem(b.getLocation(), drop);
            }
            b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
            b.setType(Material.AIR);
        }

    }



}
