package com.aystudio.core.forge.impl;

import com.aystudio.core.bukkit.util.common.ReflectionUtil;
import com.aystudio.core.forge.IForgeListenHandler;
import com.aystudio.core.forge.event.ForgeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Blank038
 * @since 2022-01-09
 */
public class AbstractForgeListenHandler implements IForgeListenHandler {
    private final String forgeMethod;
    private Class<?> forgeEventClass;

    public AbstractForgeListenHandler(String methodName, String className) {
        this.forgeMethod = methodName;
        try {
            this.forgeEventClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerListener(Plugin plugin, Listener listener, EventPriority priority) {
        RegisterManager.registerMethods(listener);
        if (!RegisterManager.METHOD_LIST.containsKey(listener)) {
            return;
        }
        RegisteredListener registeredListener = new RegisteredListener(listener, (listener1, event) -> {
            if (RegisterManager.METHOD_LIST.containsKey(listener1)) {
                RegisterManager.METHOD_LIST.get(listener1).forEach((method -> {
                    try {
                        method.invoke(listener1, ReflectionUtil.invokeMethod(event, forgeMethod));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }));
            }
            if (RegisterManager.FORGE_EVENT_METHOD_LIST.containsKey(listener1)) {
                RegisterManager.FORGE_EVENT_METHOD_LIST.get(listener1).forEach((method -> {
                    try {
                        method.invoke(listener1, new ForgeEvent((Event) ReflectionUtil.invokeMethod(event, forgeMethod)));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }));
            }
        }, priority, plugin, false);
        Object eventObj = ReflectionUtil.invokeMethod(forgeEventClass, "getHandlerList");
        ReflectionUtil.invokeMethod(eventObj, "register", new Class[]{RegisteredListener.class}, registeredListener);
    }

    @Override
    public void unregisterListener(Plugin plugin, Listener listener) {
        // 删除监听列表
        RegisterManager.FORGE_EVENT_METHOD_LIST.remove(listener);
        RegisterManager.METHOD_LIST.remove(listener);
        // 取消监听
        Object eventObj = ReflectionUtil.invokeMethod(forgeEventClass, "getHandlerList");
        ReflectionUtil.invokeMethod(eventObj, "unregister", new Class[]{RegisteredListener.class}, listener);
    }
}
