package com.yerti.ghosts.gui;

import com.yerti.ghosts.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemInputInventory implements Listener {

    Utilities utilities = new Utilities();

    //Tier, Items
    private static Map<Integer, List<ItemStack>> items = new HashMap<>();

    public Inventory getInventory(int tier) {
        //TODO: Change title
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.YELLOW + "Tier " + tier);
        if (items.get(tier) == null) return inventory;

        inventory.setContents(items.get(tier).toArray(new ItemStack[items.get(tier).size()]));
        return inventory;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory() == null) return;
        if (event.getInventory().getName() == null) return;
        if (event.getInventory().getName() == "") return;
        if (event.getInventory().getName().length() < 7) return;
        if (!isParsable(event.getInventory().getName().substring(7))) return;

        if (event.getInventory().getName().contains(ChatColor.YELLOW + "Tier") && Integer.parseInt(event.getInventory().getName().substring(7)) > 0) {
            event.getPlayer().sendMessage(utilities.savedTierContentsMessage());
            List<ItemStack> newItems = new ArrayList<>();
            for (ItemStack stack : event.getInventory().getContents()) {
                if (stack != null) {
                    newItems.add(stack);
                }
            }
            items.put(Integer.parseInt(event.getInventory().getName().substring(7)), newItems);
        }
    }

    public Map<Integer, List<ItemStack>> getItems() {
        return items;
    }



    private boolean isParsable(String input){
        try{
            Integer.parseInt(input);
            return true;
        }catch(NumberFormatException  e){
            return false;
        }
    }



}
