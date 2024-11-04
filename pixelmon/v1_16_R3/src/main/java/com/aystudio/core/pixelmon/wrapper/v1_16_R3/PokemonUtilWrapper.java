package com.aystudio.core.pixelmon.wrapper.v1_16_R3;

import com.aystudio.core.pixelmon.PokemonAPI;
import com.aystudio.core.pixelmon.wrapper.IPokemonUtilWrapper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.ServerWorldInfo;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
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
        World world = getWorld(location.getWorld().getName());
        if (world != null) {
            Pokemon pokemon = PokemonFactory.create(es);
            pokemon.getOrSpawnPixelmon(world, location.getX(), location.getY(), location.getZ());
            return pokemon;
        }
        return null;
    }

    @Override
    public World getWorld(String worldName) {
        for (ServerWorld ws : ServerLifecycleHooks.getCurrentServer().getAllLevels()) {
            if (((ServerWorldInfo) ws.getLevelData()).getLevelName().equals(worldName)) {
                return ws;
            }
        }
        return null;
    }

    @Override
    public String getPokemonUID(Pokemon pokemon) {
        CompoundNBT nbt = new CompoundNBT();
        pokemon.writeToNBT(nbt);
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
