package com.aystudio.core.pixelmon.api.data;

import java.util.UUID;

/**
 * @author Blank038, LaotouY
 */
public abstract class IPokemonLink {
    protected UUID uuid;

    public IPokemonLink(UUID uuid) {
        this.uuid = uuid;
    }

    public abstract int getLevel();

    public abstract IPokemonLink setLevel(int level);

    public abstract String getNickName();

    public abstract IPokemonLink setNickName(String nickName);

    public abstract int[] getIvStore();

    public abstract IPokemonLink setIvSotre(int[] ivs);

    public abstract int[] getEvStore();

    public abstract IPokemonLink setEvStore(int[] evs);

    /**
     * 更新记录数据
     *
     * @param slot 目标槽位
     */
    public abstract void submit(int slot);

    public abstract Boolean isShiny();

    public abstract IPokemonLink setShiny(boolean isShiny);

    public abstract String getGrowth();

    public abstract IPokemonLink setGrowth(String growthName);


    public abstract String getGender();

    public abstract IPokemonLink setGender(String genderName);

    public abstract IPokemonLink get();
}