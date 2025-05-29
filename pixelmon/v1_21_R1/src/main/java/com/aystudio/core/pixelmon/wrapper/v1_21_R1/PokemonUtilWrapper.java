package com.aystudio.core.pixelmon.wrapper.v1_21_R1;

import com.aystudio.core.pixelmon.PokemonAPI;
import com.aystudio.core.pixelmon.wrapper.IPokemonUtilWrapper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ServerLevelData;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.bukkit.Location;

/**
 * @author Blank038
 */
public class PokemonUtilWrapper implements IPokemonUtilWrapper {

    @Override
    public Pokemon spawnPokemon(EnumSpecies es, Location location) {
        return null;
    }

    @Override
    public Pokemon spawnPokemon(Species es, Location location) {
        ServerLevel world = getWorldByName(location.getWorld().getName());
        if (world != null) {
            Pokemon pokemon = PokemonFactory.create(es);
            pokemon.getOrSpawnPixelmon(world, location.getX(), location.getY(), location.getZ());
            return pokemon;
        }
        return null;
    }

    @Override
    public <T> T getWorldByName(String worldName) {
        for (ServerLevel ws : ServerLifecycleHooks.getCurrentServer().getAllLevels()) {
            if (((ServerLevelData) ws.getLevelData()).getLevelName().equals(worldName)) {
                return (T) ws;
            }
        }
        return null;
    }

    @Override
    public String getPokemonUID(Pokemon pokemon) {
        CompoundTag nbt = new CompoundTag();
        pokemon.writeToNBT(nbt, pokemon.getWorld().registryAccess());
        return nbt.getLong("UUIDLeast") + "," + nbt.getLong("UUIDMost");
    }

    @Override
    public String getPokemonName(Pokemon pokemon) {
        return getPokemonName(pokemon.getSpecies());
    }

    @Override
    public String getPokemonName(EnumSpecies species) {
        return "";
    }

    @Override
    public String getPokemonName(Species species) {
        return PokemonAPI.getInstance().getLanguage().getString(species.getTranslationKey());
    }
}
