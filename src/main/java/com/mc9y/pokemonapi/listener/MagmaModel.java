package com.mc9y.pokemonapi.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.magmafoundation.magma.api.events.ForgeEvents;

public class MagmaModel implements Listener {

    @EventHandler
    public void onForgeEvent(ForgeEvents e) {
        com.mc9y.pokemonapi.api.event.ForgeEvent forgeEvent = new com.mc9y.pokemonapi.api.event.ForgeEvent(e.getForgeEvents());
        Bukkit.getPluginManager().callEvent(forgeEvent);
    }
}