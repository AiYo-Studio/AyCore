package com.mc9y.pokemonapi.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import red.mohist.api.event.BukkitHookForgeEvent;

import java.lang.reflect.InvocationTargetException;


public class MohistModel implements IForgeListener {

    @EventHandler
    public void onForgeEvent(BukkitHookForgeEvent e) {
        com.mc9y.pokemonapi.api.event.ForgeEvent forgeEvent = new com.mc9y.pokemonapi.api.event.ForgeEvent(e.getEvent());
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
                            BukkitHookForgeEvent e = (BukkitHookForgeEvent) event;
                            method.invoke(listener1, e.getEvent());
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }));
                }
            }, priority, plugin, false);
            BukkitHookForgeEvent.getHandlerList().register(registeredListener);
        }
    }

    @Override
    public void unregisterListener(Plugin plugin, Listener listener) {
        if (RegisterManager.METHOD_LIST.containsKey(listener)) {
            BukkitHookForgeEvent.getHandlerList().unregister(listener);
        }
    }
}
