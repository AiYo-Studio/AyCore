package com.aystudio.core.pixelmon.api.core;

import com.aystudio.core.pixelmon.api.data.IPokemonLink;
import com.aystudio.core.pixelmon.api.data.PokemonLinkPG;
import com.aystudio.core.pixelmon.api.data.PokemonLinkPM;
import com.aystudio.core.bukkit.AyCore;

import java.util.UUID;

/**
 * @author Blank038
 */
public class CoreAPI {

    public static IPokemonLink createPokemonLink(UUID playerUUID, int slot) {
        switch (AyCore.getPokemonAPI().getEnumPixelmon()) {
            case PIXELMON_REFORGED:
                return new PokemonLinkPM(playerUUID, slot).get();
            case PIXELMON_GENERATIONS:
                return new PokemonLinkPG(playerUUID, slot).get();
            case NONE:
            default:
                return null;
        }
    }
}
