package com.mc9y.pokemonapi.api.storage;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.PixelmonPlayerUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerStorageUtil {

    @Deprecated
    public static EntityPlayerMP getEntityPlayerMP(String playername){
        return PixelmonPlayerUtils.getUniquePlayerStartingWith(playername);
    }

    public static void givePlayerPokemon(Player p, Pokemon pokemon){
        PlayerPartyStorage storage = Pixelmon.storageManager.getParty(p.getUniqueId());
        storage.add(pokemon);
        if(Bukkit.getPluginManager().getPlugin("PokePlate") != null && Bukkit.getPluginManager().getPlugin("PokePlate").isEnabled()){
            if(com.mc9y.pokeplate.Main.getInstance().pds.containsKey(p.getName()) && !com.mc9y.pokeplate.Main.getInstance().pds.get(p.getName()).hasPokedex(pokemon.getSpecies().name())){
                com.mc9y.pokeplate.Main.getInstance().pds.get(p.getName()).addPokedex(pokemon.getSpecies().name());
            }
        }
    }
}