package com.aystudio.core.bukkit.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CustomEvent extends PlayerEvent
        implements Cancellable {
    private static final HandlerList handler = new HandlerList();
    private final Object args;
    private final String eventName;
    private boolean cancelled;

    public CustomEvent(String eventName, Object args, Player player) {
        super(player);
        this.args = args;
        this.eventName = eventName;
    }

    public Object getArgs() {
        return args;
    }

    public String getCustomEventName() {
        return eventName;
    }

    @Override
    public HandlerList getHandlers() {
        return handler;
    }

    public static HandlerList getHandlerList() {
        return handler;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}