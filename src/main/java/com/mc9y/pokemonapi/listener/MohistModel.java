package com.mc9y.pokemonapi.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import red.mohist.api.event.BukkitHookForgeEvent;

public class MohistModel implements Listener {

    @EventHandler
    public void onForgeEvent(BukkitHookForgeEvent e) {
        com.mc9y.pokemonapi.api.event.ForgeEvent forgeEvent = new com.mc9y.pokemonapi.api.event.ForgeEvent(e.getEvent());
        Bukkit.getPluginManager().callEvent(forgeEvent);
    }
}
