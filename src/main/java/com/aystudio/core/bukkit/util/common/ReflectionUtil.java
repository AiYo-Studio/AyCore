package com.aystudio.core.bukkit.util.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Blank038
 * @since 2022-01-09
 */
public class ReflectionUtil {

    public static Object invoke(Object obj, String methodName) {
        return ReflectionUtil.invoke(obj, methodName, new Class[0]);
    }

    public static Object invoke(Object obj, String methodName, Class<?>[] paramsClass, Object... params) {
        try {
            Class<?> c = obj.getClass();
            Method method = c.getMethod(methodName, paramsClass);
            method.invoke(obj, params);
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
