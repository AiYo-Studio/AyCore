package com.aystudio.core.pixelmon.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Blank038
 * @since 2021-10-14
 */
public interface IForgeListener extends Listener{

    void registerListener(Plugin plugin, Listener listener, EventPriority priority);

    void unregisterListener(Plugin plugin, Listener listener);

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    @interface SubscribeEvent {
    }

    class RegisterManager {
        public static final HashMap<Listener, List<Method>> METHOD_LIST = new HashMap<>();

        static void registerMethods(Listener listener) {
            Class<? extends Listener> listenerClass = listener.getClass();
            for (Method method : listenerClass.getDeclaredMethods()) {
                if (method.getAnnotation(SubscribeEvent.class) != null) {
                    if (!RegisterManager.METHOD_LIST.containsKey(listener)) {
                        RegisterManager.METHOD_LIST.put(listener, new ArrayList<>());
                    }
                    RegisterManager.METHOD_LIST.get(listener).add(method);
                }
            }
        }
    }
}
