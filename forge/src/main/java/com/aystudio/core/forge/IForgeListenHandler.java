package com.aystudio.core.forge;

import com.aystudio.core.forge.event.ForgeEvent;
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
import java.util.Map;

/**
 * @author Blank038
 * @since 2021-10-14
 */
public interface IForgeListenHandler extends Listener {

    void registerListener(Plugin plugin, Listener listener, EventPriority priority);

    void unregisterListener(Plugin plugin, Listener listener);

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    @interface SubscribeEvent {
    }

    class RegisterManager {
        public static final Map<Listener, List<Method>> METHOD_LIST = new HashMap<>(),
                FORGE_EVENT_METHOD_LIST = new HashMap<>();

        public static void registerMethods(Listener listener) {
            Class<? extends Listener> listenerClass = listener.getClass();
            for (Method method : listenerClass.getDeclaredMethods()) {
                if (method.getParameterCount() != 1 || method.getAnnotation(SubscribeEvent.class) == null) {
                    continue;
                }
                if (method.getParameterTypes()[0].equals(ForgeEvent.class)) {
                    RegisterManager.FORGE_EVENT_METHOD_LIST.putIfAbsent(listener, new ArrayList<>());
                    RegisterManager.FORGE_EVENT_METHOD_LIST.get(listener).add(method);
                } else {
                    RegisterManager.METHOD_LIST.putIfAbsent(listener, new ArrayList<>());
                    RegisterManager.METHOD_LIST.get(listener).add(method);
                }
            }
        }
    }
}
