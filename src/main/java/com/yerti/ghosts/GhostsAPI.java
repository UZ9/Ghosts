package com.yerti.ghosts;

import com.yerti.ghosts.event.EventTimer;
import org.bukkit.entity.LivingEntity;

public class GhostsAPI {

    /**
     * Grabs the current ghost LivingEntity
     * @return the ghost if it's currently alive, otherwise null
     */
    public static LivingEntity getGhostEntity() {
        return EventTimer.eventEntity;
    }

}
