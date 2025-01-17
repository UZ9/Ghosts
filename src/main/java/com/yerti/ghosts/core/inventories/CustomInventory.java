package com.yerti.ghosts.core.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class CustomInventory implements InventoryHolder {

    Inventory inventory;
    InventoryHolder holder;
    private int slots;
    private String displayName;
    private ItemStack backgroundItem;



    public CustomInventory(InventoryHolder holder, int slots, String displayName, ItemStack backgroundItem) {
        this.slots = slots;
        this.displayName = displayName;
        this.backgroundItem = backgroundItem;
        this.holder = holder;

        inventory = Bukkit.createInventory(holder, slots, displayName);
    }

    public void createBackground() {
        for (int i = 0; i < slots; i++) {
            if (inventory.getContents()[i] == null || inventory.getContents()[i].getType().equals(Material.AIR)) {
                inventory.setItem(i, backgroundItem);
            }
        }
    }

    public Inventory getInventory() {
        return inventory;
    }


    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ItemStack getBackgroundItem() {
        return backgroundItem;
    }

    public void setBackgroundItem(ItemStack backgroundItem) {
        this.backgroundItem = backgroundItem;
    }
}
