package com.aystudio.core.pixelmon.listener;

import com.aystudio.core.pixelmon.api.event.ForgeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.magmafoundation.magma.api.events.ForgeEvents;

import java.lang.reflect.InvocationTargetException;

public class MagmaModel implements IForgeListener {

    @EventHandler
    public void onForgeEvent(ForgeEvents e) {
        ForgeEvent forgeEvent = new ForgeEvent(e.getForgeEvents());
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
                            ForgeEvents e = (ForgeEvents) event;
                            method.invoke(listener1, e.getForgeEvents());
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }));
                }
            }, priority, plugin, false);
            ForgeEvents.getHandlersList().register(registeredListener);
        }
    }

    @Override
    public void unregisterListener(Plugin plugin, Listener listener) {
        if (RegisterManager.METHOD_LIST.containsKey(listener)) {
            ForgeEvents.getHandlersList().unregister(listener);
        }
    }
}