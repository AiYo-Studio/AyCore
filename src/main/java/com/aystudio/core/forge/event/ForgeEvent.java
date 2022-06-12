package com.aystudio.core.forge.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Blank038
 * @since 2021-10-24
 */
public class ForgeEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final net.minecraftforge.eventbus.api.Event event;

    public ForgeEvent(net.minecraftforge.eventbus.api.Event event) {
        this.event = event;
    }

    public net.minecraftforge.eventbus.api.Event getForgeEvent() {
        return event;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}