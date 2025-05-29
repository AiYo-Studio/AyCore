package com.aystudio.core.pixelmon.api.pokemon;

import com.aystudio.core.pixelmon.PokemonAPI;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.World;
import net.minecraft.world.level.storage.ServerLevelData;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
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
    public static Pokemon spawnPokemon(Species es, Location location) {
        ServerLevel world = getWorld(location.getWorld().getName());
        if (world != null) {
            Pokemon pokemon = PokemonFactory.create(es);
            pokemon.getOrSpawnPixelmon(world, location.getX(), location.getY(), location.getZ());
            return pokemon;
        }
        return null;
    }

    public static ServerLevel getWorld(String worldName) {
        for (ServerLevel ws : ServerLifecycleHooks.getCurrentServer().getAllLevels()) {
            if (((ServerLevelData) ws.getLevelData()).getLevelName().equals(worldName)) {
                return ws;
            }
        }
        return null;
    }

    public static String getPokemonUID(Pokemon pokemon) {
        return pokemon.getUUID().toString();
    }

    @Deprecated
    public static String getPokemonName(Pokemon pokemon) {
        return getPokemonName(pokemon.getSpecies());
    }

    public static String getPokemonName(Species species) {
        return PokemonAPI.getInstance().getLanguage().getString(species.getTranslationKey());
    }
}
