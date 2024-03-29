package com.aystudio.core.bukkit.util.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Blank038
 * @since 2022-01-09
 */
public class ReflectionUtil {

    public static Object invokeMethod(Object obj, String methodName) {
        return ReflectionUtil.invokeMethod(obj, methodName, new Class[0]);
    }

    public static Object invokeMethod(Object obj, String methodName, Class<?>[] paramsClass, Object... params) {
        try {
            Class<?> c = obj instanceof Class<?> ? (Class<?>) obj : obj.getClass();
            Method method = c.getMethod(methodName, paramsClass);
            return method.invoke(obj, params);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeDeclaredMethod(Object obj, String methodName, Class<?>[] paramsClass, Object... params) {
        try {
            Class<?> c = obj instanceof Class<?> ? (Class<?>) obj : obj.getClass();
            Method method = c.getDeclaredMethod(methodName, paramsClass);
            method.setAccessible(true);
            Object result = method.invoke(obj, params);
            method.setAccessible(false);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hasClass(String c) {
        try {
            Class.forName(c);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
