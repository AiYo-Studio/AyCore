package com.aystudio.core.pixelmon.api.data;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;

import java.util.UUID;

/**
 * @author Blank038, LaotouY
 */
public class PokemonLinkPM extends IPokemonLink {
    private Pokemon pokemon;

    public PokemonLinkPM(UUID uuid, int slot) {
        super(uuid);
        PlayerPartyStorage storage = StorageProxy.getParty(uuid);
        if (storage.get(slot) == null) {
            return;
        }
        pokemon = storage.get(slot);
    }

    @Override
    public int getLevel() {
        return pokemon.getPokemonLevel();
    }

    @Override
    public IPokemonLink setLevel(int level) {
        pokemon.setLevel(level);
        return this;
    }

    @Override
    public String getNickName() {
        return pokemon.getNickname();
    }

    @Override
    public IPokemonLink setNickName(String nickName) {
        return this;
    }

    @Override
    public int[] getIvStore() {
        return pokemon.getIVs().getArray();
    }

    @Override
    public IPokemonLink setIvSotre(int[] ivs) {
        BattleStatsType[] statsTypes = BattleStatsType.values();
        for (int i = 0; i < ivs.length && i < statsTypes.length; i++) {
            pokemon.getIVs().setStat(statsTypes[i], ivs[i]);
        }
        return this;
    }

    @Override
    public int[] getEvStore() {
        return pokemon.getEVs().getArray();
    }

    @Override
    public IPokemonLink setEvStore(int[] evs) {
        BattleStatsType[] statsTypes = BattleStatsType.values();
        for (int i = 0; i < evs.length && i < statsTypes.length; i++) {
            pokemon.getEVs().setStat(statsTypes[i], evs[i]);
        }
        return this;
    }

    @Override
    public void submit(int slot) {
        PlayerPartyStorage storage = StorageProxy.getParty(uuid);
        storage.set(slot, pokemon);
    }

    @Override
    public Boolean isShiny() {
        return pokemon.isShiny();
    }

    @Override
    public IPokemonLink setShiny(boolean isShiny) {
        pokemon.setShiny(isShiny);
        return this;
    }

    @Override
    public String getGrowth() {
        return pokemon.getGrowth().name();
    }

    @Override
    public IPokemonLink setGrowth(String growthName) {
        pokemon.setGrowth(EnumGrowth.valueOf(growthName));
        return this;
    }

    @Override
    public String getGender() {
        return pokemon.getGender().name();
    }

    @Override
    public IPokemonLink setGender(String genderName) {
        pokemon.setGender(Gender.valueOf(genderName));
        return this;
    }

    @Override
    public IPokemonLink get() {
        return pokemon == null ? null : this;
    }
}
