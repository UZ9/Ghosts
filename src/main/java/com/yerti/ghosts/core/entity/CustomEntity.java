package com.yerti.ghosts.core.entity;

import org.bukkit.Effect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import javax.swing.text.html.parser.Entity;

public class CustomEntity {

    LivingEntity entity;





    public CustomEntity(LivingEntity entity, ItemStack[] armor, ItemStack hand, int health, PotionEffect[] effects) {
        this.entity = entity;

        if (armor != null) entity.getEquipment().setArmorContents(armor);
        if (hand != null) entity.getEquipment().setItemInHand(hand);
        if (health != 0) {
            entity.setMaxHealth(health);
            entity.setHealth(health);
        }
        if (effects != null) {
            for (PotionEffect effect : effects) {
                entity.addPotionEffect(effect);
            }
        }


    }


}
