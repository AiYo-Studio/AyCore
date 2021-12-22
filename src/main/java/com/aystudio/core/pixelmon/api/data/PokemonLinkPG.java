package com.aystudio.core.pixelmon.api.data;

import com.pixelmongenerations.common.entity.pixelmon.stats.links.NBTLink;
import com.pixelmongenerations.core.Pixelmon;
import com.pixelmongenerations.core.enums.EnumGrowth;
import com.pixelmongenerations.core.network.PixelmonData;
import com.pixelmongenerations.core.network.packetHandlers.clientStorage.Add;
import com.pixelmongenerations.core.storage.NbtKeys;
import com.pixelmongenerations.core.storage.PixelmonStorage;
import com.pixelmongenerations.core.storage.PlayerStorage;
import com.pixelmongenerations.common.entity.pixelmon.stats.*;

import java.util.UUID;

/**
 * @author Blank038, LaotouY
 */
public class PokemonLinkPG extends IPokemonLink {
    private NBTLink link;

    public PokemonLinkPG(UUID uuid, int slot) {
        super(uuid);
        PlayerStorage storage = PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(uuid).orElse(null);
        if (slot >= storage.partyPokemon.length || storage.partyPokemon[slot] == null) {
            return;
        }
        link = new NBTLink(storage.partyPokemon[slot]);
    }

    @Override
    public int getLevel() {
        return link.getLevel();
    }

    @Override
    public IPokemonLink setLevel(int level) {
        link.setLevel(level);
        return this;
    }

    @Override
    public String getNickName() {
        return link.getNickname();
    }

    @Override
    public IPokemonLink setNickName(String nickName) {
        link.getNBT().setString(NbtKeys.NICKNAME, nickName);
        return this;
    }

    @Override
    public int[] getIvStore() {
        return link.getStats().IVs.getArray();
    }

    @Override
    public IPokemonLink setIvSotre(int[] ivs) {
        link.getStats().IVs.copyIVs(new IVStore(ivs));
        return this;
    }

    @Override
    public int[] getEvStore() {
        EVsStore evs = link.getStats().EVs;
        return new int[]{evs.HP, evs.Attack, evs.Defence, evs.SpecialAttack, evs.SpecialDefence, evs.Speed};
    }

    @Override
    public IPokemonLink setEvStore(int[] evs) {
        link.getStats().EVs.HP = evs[0];
        link.getStats().EVs.Attack = evs[1];
        link.getStats().EVs.Defence = evs[2];
        link.getStats().EVs.SpecialAttack = evs[3];
        link.getStats().EVs.SpecialDefence = evs[4];
        link.getStats().EVs.Speed = evs[5];
        return this;
    }

    @Override
    public void submit(int slot) {
        PlayerStorage storage = PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(uuid).orElse(null);
        storage.partyPokemon[slot] = link.getNBT();
        Pixelmon.NETWORK.sendTo(new Add(new PixelmonData(link.getNBT())), storage.getPlayer());
    }

    @Override
    public Boolean isShiny() {
        return link.isShiny();
    }

    @Override
    public IPokemonLink setShiny(boolean isShiny) {
        link.getNBT().setBoolean(NbtKeys.SHINY, isShiny);
        return this;
    }

    @Override
    public String getGrowth() {
        return link.getGrowth().name();
    }

    @Override
    public IPokemonLink setGrowth(String growthName) {
        link.getNBT().setInteger(NbtKeys.GROWTH, EnumGrowth.valueOf(growthName).index);
        return this;
    }

    @Override
    public String getGender() {
        return link.getGender().name();
    }

    @Override
    public IPokemonLink setGender(String genderName) {
        link.getNBT().setShort(NbtKeys.GENDER, (short) Gender.getGender(genderName).ordinal());
        return this;
    }

    @Override
    public IPokemonLink get() {
        return link == null ? null : this;
    }
}
