package com.aystudio.core.pixelmon.listener;

import catserver.api.bukkit.event.ForgeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.InvocationTargetException;

public class CatServerModel implements IForgeListener {

    @EventHandler
    public void onListener(ForgeEvent event) {
        com.aystudio.core.pixelmon.api.event.ForgeEvent forgeEvent = new com.aystudio.core.pixelmon.api.event.ForgeEvent(event.getForgeEvent());
        Bukkit.getPluginManager().callEvent(forgeEvent);
    }

    @Override
    public void registerListener(Plugin plugin, Listener listener, EventPriority priority) {
        RegisterManager.registerMethods(listener);
        if (RegisterManager.METHOD_LIST.containsKey(listener)) {
            RegisteredListener registeredListener = new RegisteredListener(listener, (listener1, event) -> {
                if (RegisterManager.METHOD_LIST.containsKey(listener1)) {
                    RegisterManager.METHOD_LIST.get(listener1).forEach((method -> {
                        if (method.getParameterCount() == 0) {
                            return;
                        }
                        try {
                            ForgeEvent e = (ForgeEvent) event;
                            method.invoke(listener1, e.getForgeEvent());
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }));
                }
            }, priority, plugin, false);
            ForgeEvent.getHandlerList().register(registeredListener);
        }
    }

    @Override
    public void unregisterListener(Plugin plugin, Listener listener) {
        if (RegisterManager.METHOD_LIST.containsKey(listener)) {
            ForgeEvent.getHandlerList().unregister(listener);
        }
    }
}