package com.yerti.ghosts.core.items;


import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItemStack extends ItemStack {

    Material type;
    int amount;


    public CustomItemStack(Material type, int amount) {
        this(type, amount, null, false);
    }




    public CustomItemStack(Material type, int amount, String displayName, boolean glowing) {

        this.type = type;
        this.amount = amount;
        this.setType(type);
        ItemMeta meta = getItemMeta();
        if (displayName != null) {

            meta.setDisplayName(displayName);
        }
        this.setAmount(amount);

        ItemStack stack = new ItemStack(Material.STONE, 3, (short) 3);



        if (glowing) {

            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        this.setItemMeta(meta);

        if (glowing) {
            this.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
        }
    }

    public ItemStack getStack() {
        return this;
    }





}
