package com.mc9y.pokemonapi.listener;

import catserver.api.bukkit.event.ForgeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CatServerModel implements Listener {

    @EventHandler
    public void onListener(ForgeEvent event) {
        com.mc9y.pokemonapi.api.event.ForgeEvent forgeEvent = new com.mc9y.pokemonapi.api.event.ForgeEvent(event.getForgeEvent());
        Bukkit.getPluginManager().callEvent(forgeEvent);
    }
}