package com.aystudio.core.bukkit.command.child;

import com.aystudio.core.bukkit.command.registry.CustomCommand;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Blank038
 */
public class ExecuteMethod {
    private final CustomCommand customCommand;
    private final Method method;
    private final Object c;

    public ExecuteMethod(Object object, CustomCommand customCommand, Method method) {
        this.c = object;
        this.customCommand = customCommand;
        this.method = method;
    }

    public void perform(CommandSender sender, String[] args, String command) {
        if (!customCommand.defaultHasPermission() && !"".equals(customCommand.permission()) && !sender.hasPermission(customCommand.permission())) {
            if (!"".equals(customCommand.notPermissionText())) {
                sender.sendMessage(customCommand.notPermissionText());
            }
            return;
        }
        try {
            switch (method.getParameterCount()) {
                case 1:
                    method.invoke(c, sender);
                    break;
                case 2:
                    method.invoke(c, sender, args);
                    break;
                default:
                    method.invoke(c, sender, args, command);
                    break;
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
