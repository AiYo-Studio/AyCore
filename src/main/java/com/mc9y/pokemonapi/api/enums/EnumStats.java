package com.mc9y.pokemonapi.api.enums;

public enum EnumStats {
    IVS_HP,
    IVS_Attack,
    IVS_Speed,
    IVS_Defence,
    IVS_SpecialAttack,
    IVS_SpecialDefence,
    EVS_HP,
    EVS_Attack,
    EVS_Speed,
    EVS_Defence,
    EVS_SpecialAttack,
    EVS_SpecialDefence,
    Level,
    Owner,
    Shiny,
    Growth,
    Gender,
    Nature,
    Move1,
    Move2,
    Move3,
    Move4,
    IVS_SUM,
    EVS_SUM,
    Nick_Name,
    Ability,
    OriginalTrainer,
    Ability_DES,
    TRANSLATE_NAME,
    SPECIE_NAME;

    public static boolean has(String key) {
        try {
            EnumStats.valueOf(key);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}