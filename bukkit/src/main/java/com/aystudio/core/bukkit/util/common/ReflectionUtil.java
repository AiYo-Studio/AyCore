package com.aystudio.core.bukkit.util.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Blank038
 * @since 2022-01-09
 */
public class ReflectionUtil {

    private static final ConcurrentHashMap<String, Method> METHOD_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Method> DECLARED_METHOD_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Boolean> CLASS_CACHE = new ConcurrentHashMap<>();

    public static Object invokeMethod(Object obj, String methodName) {
        return ReflectionUtil.invokeMethod(obj, methodName, new Class[0]);
    }

    public static Object invokeMethod(Object obj, String methodName, Class<?>[] paramsClass, Object... params) {
        try {
            Class<?> c = obj instanceof Class<?> ? (Class<?>) obj : obj.getClass();
            String cacheKey = generateMethodCacheKey(c, methodName, paramsClass);
            Method method = METHOD_CACHE.computeIfAbsent(cacheKey, k -> {
                try {
                    return c.getMethod(methodName, paramsClass);
                } catch (NoSuchMethodException e) {
                    return null;
                }
            });
            if (method != null) {
                return method.invoke(obj, params);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeDeclaredMethod(Object obj, String methodName, Class<?>[] paramsClass, Object... params) {
        try {
            Class<?> c = obj instanceof Class<?> ? (Class<?>) obj : obj.getClass();
            String cacheKey = generateMethodCacheKey(c, methodName, paramsClass);
            Method method = DECLARED_METHOD_CACHE.computeIfAbsent(cacheKey, k -> {
                try {
                    Method m = c.getDeclaredMethod(methodName, paramsClass);
                    m.setAccessible(true);
                    return m;
                } catch (NoSuchMethodException e) {
                    return null;
                }
            });
            if (method != null) {
                return method.invoke(obj, params);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hasClass(String c) {
        return CLASS_CACHE.computeIfAbsent(c, className -> {
            try {
                Class.forName(className);
                return true;
            } catch (Exception ignored) {
                return false;
            }
        });
    }

    private static String generateMethodCacheKey(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append(clazz.getName()).append("#").append(methodName);
        if (paramTypes != null && paramTypes.length > 0) {
            sb.append("(");
            for (int i = 0; i < paramTypes.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(paramTypes[i].getName());
            }
            sb.append(")");
        } else {
            sb.append("()");
        }
        return sb.toString();
    }
}
