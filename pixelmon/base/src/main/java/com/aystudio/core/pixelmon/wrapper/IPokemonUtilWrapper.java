package com.aystudio.core.pixelmon.wrapper;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.world.World;
import org.bukkit.Location;

public interface IPokemonUtilWrapper {

    Pokemon spawnPokemon(EnumSpecies es, Location location);

    Pokemon spawnPokemon(Species es, Location location);

    World getWorld(String worldName);

    String getPokemonUID(Pokemon pokemon);

    String getPokemonName(Pokemon pokemon);

    String getPokemonName(EnumSpecies species);

    String getPokemonName(Species species);
}
