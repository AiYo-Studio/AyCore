package com.mc9y.pokemonapi.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ForgeEvent extends Event {
    private static final HandlerList handler = new HandlerList();
    private net.minecraftforge.fml.common.eventhandler.Event event;

    public ForgeEvent(net.minecraftforge.fml.common.eventhandler.Event event) {
        this.event = event;
    }

    public net.minecraftforge.fml.common.eventhandler.Event getForgeEvent() {
        return event;
    }

    @Override
    public HandlerList getHandlers() {
        return handler;
    }

    public static HandlerList getHandlerList() {
        return handler;
    }
}