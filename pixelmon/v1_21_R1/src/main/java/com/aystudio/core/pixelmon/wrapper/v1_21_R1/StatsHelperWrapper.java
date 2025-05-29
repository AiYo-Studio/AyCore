package com.aystudio.core.pixelmon.wrapper.v1_21_R1;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.pixelmon.api.enums.EnumStats;
import com.aystudio.core.pixelmon.api.pokemon.PokemonUtil;
import com.aystudio.core.pixelmon.api.stats.StatsHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.ImmutableAttack;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Blank038
 */
public class StatsHelperWrapper extends StatsHelper {

    @Override
    public Map<EnumStats, Object> getStats(Pokemon pokemon) {
        Map<EnumStats, Object> result = new HashMap<>();
        DecimalFormat df = new DecimalFormat("#0.##");
        int ivSum = pokemon.getIVs().getStat(BattleStatsType.HP) + pokemon.getIVs().getStat(BattleStatsType.ATTACK) + pokemon.getIVs().getStat(BattleStatsType.DEFENSE) +
                pokemon.getIVs().getStat(BattleStatsType.SPECIAL_ATTACK) + pokemon.getIVs().getStat(BattleStatsType.SPECIAL_DEFENSE) + pokemon.getIVs().getStat(BattleStatsType.SPEED);
        int evSum = pokemon.getEVs().getStat(BattleStatsType.HP) + pokemon.getEVs().getStat(BattleStatsType.ATTACK) + pokemon.getEVs().getStat(BattleStatsType.DEFENSE) +
                pokemon.getEVs().getStat(BattleStatsType.SPECIAL_ATTACK) + pokemon.getEVs().getStat(BattleStatsType.SPECIAL_DEFENSE) + pokemon.getEVs().getStat(BattleStatsType.SPEED);
        String totalEVs = df.format((int) (evSum / 510.0 * 100.0)) + "%";
        String totalIVs = df.format((int) (ivSum / 186.0 * 100.0)) + "%";
        String nickname = (pokemon.getNickname() == null) ? PokemonUtil.getPokemonName(pokemon.getSpecies()) : pokemon.getNickname().getString();
        String shiny = AyCore.getInstance().getConfig().getString("text.shiny." + pokemon.isShiny(), "<ERROR>");
        int level = pokemon.getPokemonLevel();
        String nature = pa.getLanguage().getString(pokemon.getNature().getTranslationKey());
        String growth = pa.getLanguage().getString(pokemon.getGrowth().getRegisteredName());
        String ability = pa.getLanguage().getString(pokemon.getAbility().getTranslationKey());
        String ability_des = pa.getLanguage().getString(pokemon.getAbility().getTranslationKey() + ".description"),
                nullOption = AyCore.getInstance().getConfig().getString("text.error", "<ERROR>");
        String originalTrainer = pokemon.getOriginalTrainer() == null ? nullOption : pokemon.getOriginalTrainer();
        for (int i = 0; i < 4; i++) {
            result.put(EnumStats.valueOf("Move" + (i + 1)), (pokemon.getMoveset().get(i) != null) ?
                    (pa.getLanguage().getString(this.getAttackBase(pokemon.getMoveset().get(i)).toLowerCase())) : nullOption);
        }
        result.put(EnumStats.IVS_HP, pokemon.getIVs().getStat(BattleStatsType.HP));
        result.put(EnumStats.IVS_Attack, pokemon.getIVs().getStat(BattleStatsType.ATTACK));
        result.put(EnumStats.IVS_Speed, pokemon.getIVs().getStat(BattleStatsType.SPEED));
        result.put(EnumStats.IVS_Defence, pokemon.getIVs().getStat(BattleStatsType.DEFENSE));
        result.put(EnumStats.IVS_SpecialAttack, pokemon.getIVs().getStat(BattleStatsType.SPECIAL_ATTACK));
        result.put(EnumStats.IVS_SpecialDefence, pokemon.getIVs().getStat(BattleStatsType.SPECIAL_DEFENSE));
        result.put(EnumStats.EVS_HP, pokemon.getEVs().getStat(BattleStatsType.HP));
        result.put(EnumStats.EVS_Attack, pokemon.getEVs().getStat(BattleStatsType.ATTACK));
        result.put(EnumStats.EVS_Speed, pokemon.getEVs().getStat(BattleStatsType.SPEED));
        result.put(EnumStats.EVS_Defence, pokemon.getEVs().getStat(BattleStatsType.DEFENSE));
        result.put(EnumStats.EVS_SpecialAttack, pokemon.getEVs().getStat(BattleStatsType.SPECIAL_ATTACK));
        result.put(EnumStats.EVS_SpecialDefence, pokemon.getEVs().getStat(BattleStatsType.SPECIAL_DEFENSE));
        result.put(EnumStats.Level, level);
        result.put(EnumStats.Owner, pokemon.getOwnerPlayer() != null ? pokemon.getOwnerPlayer().getName().getString() : "无");
        result.put(EnumStats.Shiny, shiny);
        result.put(EnumStats.Growth, growth);
        String gender = AyCore.getInstance().getConfig().getString("text.gender." + pokemon.getGender().name().toLowerCase());
        result.put(EnumStats.Gender, gender);
        result.put(EnumStats.Nature, nature);
        result.put(EnumStats.IVS_SUM, totalIVs);
        result.put(EnumStats.EVS_SUM, totalEVs);
        result.put(EnumStats.Nick_Name, nickname);
        result.put(EnumStats.Ability, ability);
        result.put(EnumStats.Ability_DES, ability_des);
        result.put(EnumStats.OriginalTrainer, originalTrainer);
        result.put(EnumStats.TRANSLATE_NAME, PokemonUtil.getPokemonName(pokemon.getSpecies()));
        result.put(EnumStats.SPECIE_NAME, pokemon.getSpecies().getName());
        result.put(EnumStats.SPEC_FLAG_UNTRADEABLE, AyCore.getInstance().getConfig().getString("text.common." + pokemon.hasFlag("untradeable")));
        result.put(EnumStats.SPEC_FLAG_UNBTREEDABLE, AyCore.getInstance().getConfig().getString("text.common." + pokemon.hasFlag("unbreedable")));
        return result;
    }

    public String getAttackBase(Attack ms) {
        String attackName = "";
        Object object = getField(ms.getClass(), "baseAttack", ms);
        ImmutableAttack ab = (object != null ? (ImmutableAttack) object : null);
        if (ab != null) {
            attackName = ab.getTranslationKey();
        }
        return attackName;
    }
}
