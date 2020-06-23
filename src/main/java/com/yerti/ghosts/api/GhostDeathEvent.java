package com.yerti.ghosts.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;


public class GhostDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private List<Player> players;
    private int tier;

    public GhostDeathEvent(List<Player> players, int tier) {
        this.players = players;
        this.tier = tier;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getTier() {
        return tier;
    }
}
