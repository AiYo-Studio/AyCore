package com.mc9y.pokemonapi.api.core;

import com.mc9y.blank038api.Blank038API;
import com.mc9y.pokemonapi.api.data.IPokemonLink;
import com.mc9y.pokemonapi.api.data.PokemonLinkPG;
import com.mc9y.pokemonapi.api.data.PokemonLinkPM;

import java.util.UUID;

/**
 * @author Blank038
 */
public class CoreAPI {

    public static IPokemonLink createPokemonLink(UUID playerUUID, int slot) {
        switch (Blank038API.getPokemonAPI().getEnumPixelmon()) {
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
