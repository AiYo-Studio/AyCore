package com.aystudio.core.pixelmon.wrapper.v1_12_R1;

import com.aystudio.core.pixelmon.PokemonAPI;
import com.aystudio.core.pixelmon.api.pokemon.PokemonUtil;
import com.aystudio.core.pixelmon.wrapper.IPokemonApiWrapper;

/**
 * @author Blank038
 */
public class PokemonApiWrapper implements IPokemonApiWrapper {

    public PokemonApiWrapper() {
        PokemonAPI api = PokemonAPI.getInstance();
        api.setLanguage(new I18nWrapper());
        api.setSpriteHelper(new SpriteHelperWrapper());
        api.setStatsHelper(new StatsHelperWrapper());
    }
}
