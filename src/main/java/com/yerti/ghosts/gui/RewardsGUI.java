package com.yerti.ghosts.gui;

import com.yerti.ghosts.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RewardsGUI implements Listener {


    Utilities utilities = new Utilities();

    public Inventory getInventory(int tier) {
        Inventory inventory = Bukkit.createInventory(null, 54, utilities.getRewardsGuiTitle());
        ItemInputInventory rewardInventory = new ItemInputInventory();

        if (rewardInventory.getItems().get(tier) == null) {
            return inventory;
        }
        List<ItemStack> rewards = rewardInventory.getItems().get(tier);
        ItemStack[] items = rewards.toArray(new ItemStack[rewards.size()]);
        //rewards

        int rewardCount = items.length < 5 ? items.length : 5;

        for (int i = 0; i < rewardCount; i++) {
            int slot = findRandomSlot(inventory);

            int randomReward = Utilities.getRandomNumberInRange(0, items.length - 1);

            inventory.setItem(slot, items[randomReward]);
        }

        return inventory;
    }


    //null, null, e, null

    private int findRandomSlot(Inventory inventory) {

        int randomNumber = Utilities.getRandomNumberInRange(0, inventory.getSize() - 1);

        return randomNumber;
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getOpenInventory() != null) {
            if (event.getWhoClicked().getOpenInventory().getTopInventory() != null) {
                if (event.getWhoClicked().getOpenInventory().getTopInventory().getName().equals(utilities.getRewardsGuiTitle())) {

                    if (event.getRawSlot() >= 54) {
                        event.setCancelled(true);
                    }
                }
            }
        }

    }


}

