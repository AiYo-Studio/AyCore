package com.mc9y.pokemonapi.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomEvent extends Event
        implements Cancellable {
    private static final HandlerList handler = new HandlerList();
    private Object args;
    private String eventName;
    private boolean cancelled;
    private Player player;

    public CustomEvent(String eventName, Object args, Player player) {
        this.args = args;
        this.eventName = eventName;
        this.player = player;
    }

    public Object getArgs() {
        return args;
    }

    public Player getPlayer() {
        return player;
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