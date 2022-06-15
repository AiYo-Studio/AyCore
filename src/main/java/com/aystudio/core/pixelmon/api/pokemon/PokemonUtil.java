package com.aystudio.core.pixelmon.api.pokemon;

import com.aystudio.core.bukkit.AyCore;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.ServerWorldInfo;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.bukkit.Location;

/**
 * @author Blank038
 */
public class PokemonUtil {
    private static final MinecraftServer MINECRAFT_SERVER = ServerLifecycleHooks.getCurrentServer();

    /**
     * 在某个坐标生成某个精灵, 返回 Pokemon
     *
     * @param es       精灵类型
     * @param location 目标坐标
     * @return 生成精灵
     */
    public static Pokemon spawnPokemon(Species es, Location location) {
        World world = getWorld(location.getWorld().getName());
        if (world != null) {
            Pokemon pokemon = PokemonFactory.create(es);
            pokemon.getOrSpawnPixelmon(world, location.getX(), location.getY(), location.getZ());
            return pokemon;
        }
        return null;
    }

    public static World getWorld(String worldName) {
        for (ServerWorld ws : MINECRAFT_SERVER.getAllLevels()) {
            if (((ServerWorldInfo) ws.getLevelData()).getLevelName().equals(worldName)) {
                return ws;
            }
        }
        return null;
    }

    public static String getPokemonUID(Pokemon pokemon) {
        CompoundNBT nbt = new CompoundNBT();
        pokemon.writeToNBT(nbt);
        return nbt.getLong("UUIDLeast") + "," + nbt.getLong("UUIDMost");
    }

    @Deprecated
    public static String getPokemonName(Pokemon pokemon) {
        return getPokemonName(pokemon.getSpecies());
    }

    public static String getPokemonName(Species species) {
        return AyCore.getPokemonAPI().getLanguage().getString("pixelmon." + species.getName().toLowerCase() + ".name");
    }
}