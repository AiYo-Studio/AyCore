package com.aystudio.core.pixelmon.wrapper.v1_12_R1;

import com.aystudio.core.pixelmon.PokemonAPI;
import com.aystudio.core.pixelmon.wrapper.IPokemonUtilWrapper;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.bukkit.Location;

/**
 * @author Blank038
 */
public class PokemonUtilWrapper implements IPokemonUtilWrapper {

    @Override
    public Pokemon spawnPokemon(EnumSpecies es, Location location) {
        World world = getWorld(location.getWorld().getName());
        if (world != null) {
            Pokemon pokemon = Pixelmon.pokemonFactory.create(es);
            pokemon.getOrSpawnPixelmon(world, location.getX(), location.getY(), location.getZ());
            return pokemon;
        }
        return null;
    }

    @Override
    public Pokemon spawnPokemon(Species es, Location location) {
        return null;
    }

    @Override
    public World getWorld(String worldName) {
        for (WorldServer ws : FMLCommonHandler.instance().getMinecraftServerInstance().worlds) {
            if (ws.getWorldInfo().getWorldName().equals(worldName)) {
                return ws;
            }
        }
        return null;
    }

    @Override
    public String getPokemonUID(Pokemon pokemon) {
        NBTTagCompound nbt = new NBTTagCompound();
        pokemon.writeToNBT(nbt);
        return nbt.getLong("UUIDLeast") + "," + nbt.getLong("UUIDMost");
    }

    @Deprecated
    @Override
    public String getPokemonName(Pokemon pokemon) {
        return getPokemonName(pokemon.getSpecies());
    }

    @Override
    public String getPokemonName(EnumSpecies species) {
        return PokemonAPI.getInstance().getLanguage().getString("pixelmon." + species.getPokemonName().toLowerCase() + ".name");
    }

    @Override
    public String getPokemonName(Species species) {
        return null;
    }
}
