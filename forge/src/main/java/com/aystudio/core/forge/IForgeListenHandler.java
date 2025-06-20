package com.aystudio.core.forge;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Blank038
 */
public interface IForgeListenHandler extends Listener {

    void registerListener(Plugin plugin, Listener listener, EventPriority priority);

    void unregisterListener(Plugin plugin, Listener listener);

    net.minecraftforge.fml.common.eventhandler.Event convertEvent(Event event);

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    @interface SubscribeEvent {
    }
}
