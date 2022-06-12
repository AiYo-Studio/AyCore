package com.aystudio.core.pixelmon.api.storage;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.PixelmonPlayerUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerStorageUtil {

    @Deprecated
    public static ServerPlayerEntity getEntityPlayerMP(String playername){
        return PixelmonPlayerUtils.getUniquePlayerStartingWith(playername);
    }

    public static void givePlayerPokemon(Player p, Pokemon pokemon){
        PlayerPartyStorage storage = StorageProxy.getParty(p.getUniqueId());
        storage.add(pokemon);
        if(Bukkit.getPluginManager().getPlugin("PokePlate") != null && Bukkit.getPluginManager().getPlugin("PokePlate").isEnabled()){
            if(com.mc9y.pokeplate.Main.getInstance().pds.containsKey(p.getName()) && !com.mc9y.pokeplate.Main.getInstance().pds.get(p.getName()).hasPokedex(pokemon.getSpecies().getName())){
                com.mc9y.pokeplate.Main.getInstance().pds.get(p.getName()).addPokedex(pokemon.getSpecies().getName());
            }
        }
    }
}