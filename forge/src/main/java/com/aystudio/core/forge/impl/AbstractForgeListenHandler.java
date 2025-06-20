package com.aystudio.core.forge.impl;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.util.common.ReflectionUtil;
import com.aystudio.core.common.util.Pair;
import com.aystudio.core.forge.IForgeListenHandler;
import com.aystudio.core.forge.event.ForgeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * @author Blank038
 */
public class AbstractForgeListenHandler implements IForgeListenHandler {
    private Class<?> forgeEventClass;

    public AbstractForgeListenHandler(String className) {
        try {
            this.forgeEventClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            AyCore.getInstance().getLogger().log(Level.SEVERE, e, () -> String.format("Failed to hook %s", className));
        }
    }

    @Override
    public void registerListener(Plugin plugin, Listener listener, EventPriority priority) {
        Class<? extends Listener> clazz = listener.getClass();
        List<Pair<Consumer<Object>, Boolean>> result = this.forewardMethods(clazz, listener);
        if (result.isEmpty()) {
            return;
        }
        RegisteredListener registeredListener = new RegisteredListener(listener, (var1, var2) -> {
            net.minecraftforge.fml.common.eventhandler.Event convertedEvent = this.convertEvent(var2);
            ForgeEvent event = new ForgeEvent(convertedEvent);
            result.forEach((v) -> v.getKey().accept(v.getValue() ? event : convertedEvent));
        }, priority, plugin, false);
        Object eventObj = ReflectionUtil.invokeMethod(forgeEventClass, "getHandlerList");
        ReflectionUtil.invokeMethod(eventObj, "register", new Class[]{RegisteredListener.class}, registeredListener);
    }

    private List<Pair<Consumer<Object>, Boolean>> forewardMethods(Class<? extends Listener> clazz, Listener listener) {
        List<Pair<Consumer<Object>, Boolean>> result = new ArrayList<>();
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getParameterCount() != 1 || method.getAnnotation(SubscribeEvent.class) == null) {
                continue;
            }
            method.setAccessible(true);
            String methodName = method.getName();
            Class<?> parameterType = method.getParameterTypes()[0];
            try {
                MethodHandle methodHandle = lookup.findVirtual(clazz, method.getName(), MethodType.methodType(void.class, parameterType));
                MethodType consumerFactoryType = MethodType.methodType(Consumer.class, clazz);
                MethodType consumerInterfaceType = MethodType.methodType(void.class, Object.class);
                MethodType actualMethodType = MethodType.methodType(void.class, parameterType);
                CallSite callSite = LambdaMetafactory.metafactory(
                        lookup,
                        "accept",
                        consumerFactoryType,
                        consumerInterfaceType,
                        methodHandle,
                        actualMethodType
                );
                Consumer<Object> k = (Consumer<Object>) callSite.getTarget().invoke(listener);
                result.add(new Pair<>(k, parameterType.equals(ForgeEvent.class)));
            } catch (Throwable e) {
                AyCore.getInstance().getLogger().log(Level.WARNING, e, () -> "Failed to forward forge event.");
            }
        }
        return result;
    }

    @Override
    public void unregisterListener(Plugin plugin, Listener listener) {
        // 取消监听
        Object eventObj = ReflectionUtil.invokeMethod(forgeEventClass, "getHandlerList");
        ReflectionUtil.invokeMethod(eventObj, "unregister", new Class[]{RegisteredListener.class}, listener);
    }

    @Override
    public Event convertEvent(org.bukkit.event.Event event) {
        return null;
    }
}
