package com.mc9y.blank038api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KeyEvent extends Event {
    private static final HandlerList handler = new HandlerList();
    private Player player;
    private int key;
    private boolean down;

    public KeyEvent(Player player, int key, boolean down) {
        this.player = player;
        this.key = key;
        this.down = down;
    }

    public Player getPlayer() {
        return player;
    }

    public int getKey() {
        return key;
    }

    public boolean isDown() {
        return down;
    }

    @Override
    public HandlerList getHandlers() {
        return handler;
    }

    public static HandlerList getHandlerList() {
        return handler;
    }
}
