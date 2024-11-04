package com.aystudio.core.bukkit.command.child;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.command.registry.CustomCommand;
import com.aystudio.core.bukkit.exception.ClassExistedException;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Blank038
 */
public class BaseCommand {
    private final List<String> classes = new ArrayList<>();
    private final Map<String, ExecuteMethod> methodMap = new HashMap<>();
    private final List<ExecuteMethod> notParametersMethods = new ArrayList<>();

    /**
     * 增加新的命令监听类
     *
     * @param c 监听的类
     */
    public void addClass(Object c) {
        Class<?> ac = c.getClass();
        if (classes.contains(ac.getName())) {
            try {
                throw new ClassExistedException("Class " + ac.getName() + " is exists.");
            } catch (ClassExistedException e) {
                AyCore.getInstance().getLogger().severe(e.toString());
            }
            return;
        }
        loadClass(c);
        classes.add(ac.getName());
    }

    private void loadClass(Object c) {
        Class<?> cl = c.getClass();
        for (Method method : cl.getMethods()) {
            if (method.isAnnotationPresent(CustomCommand.class)) {
                CustomCommand ac = method.getAnnotation(CustomCommand.class);
                if ("".equals(ac.sub())) {
                    notParametersMethods.add(new ExecuteMethod(c, ac, method));
                } else {
                    methodMap.put(ac.sub(), new ExecuteMethod(c, ac, method));
                }
            }
        }
    }

    public void perform(CommandSender sender, String[] args, String command) {
        if (args.length == 0) {
            for (ExecuteMethod method : notParametersMethods) {
                method.perform(sender, args, command);
            }
        } else if (methodMap.containsKey(args[0])) {
            methodMap.get(args[0]).perform(sender, args, command);
        }
    }
}