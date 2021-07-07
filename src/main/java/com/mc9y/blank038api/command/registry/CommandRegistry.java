package com.mc9y.blank038api.command.registry;

import com.mc9y.blank038api.command.child.BaseCommand;
import com.mc9y.blank038api.exception.ErrorJavaPluginException;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandRegistry {
    private final HashMap<String, BaseCommand> pluginCommands = new HashMap<>();
    private final HashMap<String, String> aliases = new HashMap<>();

    public void registerCommand(Plugin plugin, Object[] classes, String[] aliases) {
        if (plugin == null) {
            try {
                throw new ErrorJavaPluginException("JavaPlugin class is null.");
            } catch (ErrorJavaPluginException e) {
                e.printStackTrace();
            }
            return;
        }
        String name = plugin.getDescription().getName();
        pluginCommands.remove(name);
        pluginCommands.put(name, new BaseCommand());
        for (Object c : classes) {
            pluginCommands.get(name).addClass(c);
        }
        for (String i : aliases) {
            this.aliases.put(i, name);
        }
    }

    public void unregisterCommand(Plugin plugin) {
        String pluginName = plugin.getName();
        if (!pluginCommands.containsKey(pluginName)) return;
        List<String> removes = new ArrayList<>();
        for (Map.Entry<String, String> entry : aliases.entrySet()) {
            if (entry.getValue().equals(pluginName)) {
                removes.add(entry.getKey());
            }
        }
        removes.forEach(aliases::remove);
        // 取消命令类
        pluginCommands.remove(pluginName);
    }

    public boolean performCommand(CommandSender sender, String command, String[] args) {
        if (aliases.containsKey(command)) {
            pluginCommands.get(aliases.get(command)).perform(sender, args, command);
            return true;
        }
        return false;
    }
}