package com.mc9y.pokemonapi.api.pokemon;

import com.mc9y.blank038api.Blank038API;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.bukkit.Location;

/**
 * @author Blank038
 */
public class PokemonUtil {

    /**
     * 在某个坐标生成某个精灵, 返回 Pokemon
     *
     * @param es       精灵类型
     * @param location 目标坐标
     * @return 生成精灵
     */
    public static Pokemon spawnPokemon(EnumSpecies es, Location location) {
        World world = getWorld(location.getWorld().getName());
        if (world != null) {
            Pokemon pokemon = Pixelmon.pokemonFactory.create(es);
            pokemon.getOrSpawnPixelmon(world, location.getX(), location.getY(), location.getZ());
            return pokemon;
        }
        return null;
    }

    public static World getWorld(String worldName) {
        for (WorldServer ws : FMLCommonHandler.instance().getMinecraftServerInstance().worlds) {
            if (ws.getWorldInfo().getWorldName().equals(worldName)) {
                return ws;
            }
        }
        return null;
    }

    public static String getPokemonUID(Pokemon pokemon) {
        NBTTagCompound nbt = new NBTTagCompound();
        pokemon.writeToNBT(nbt);
        return nbt.getLong("UUIDLeast") + "," + nbt.getLong("UUIDMost");
    }

    @Deprecated
    public static String getPokemonName(Pokemon pokemon) {
        return getPokemonName(pokemon.getSpecies());
    }

    public static String getPokemonName(EnumSpecies species) {
        return Blank038API.getPokemonAPI().getLang().getString("pixelmon." + species.getPokemonName().toLowerCase() + ".name");
    }
}