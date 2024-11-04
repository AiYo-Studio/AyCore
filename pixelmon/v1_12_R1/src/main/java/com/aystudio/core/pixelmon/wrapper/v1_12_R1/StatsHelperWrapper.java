package com.aystudio.core.pixelmon.wrapper.v1_12_R1;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.pixelmon.api.enums.EnumStats;
import com.aystudio.core.pixelmon.api.pokemon.PokemonUtil;
import com.aystudio.core.pixelmon.api.stats.StatsHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

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
        int ivSum = pokemon.getIVs().getStat(StatsType.HP) + pokemon.getIVs().getStat(StatsType.Attack) + pokemon.getIVs().getStat(StatsType.Defence) +
                pokemon.getIVs().getStat(StatsType.SpecialAttack) + pokemon.getIVs().getStat(StatsType.SpecialDefence) + pokemon.getIVs().getStat(StatsType.Speed);
        int evSum = pokemon.getEVs().getStat(StatsType.HP) + pokemon.getEVs().getStat(StatsType.Attack) + pokemon.getEVs().getStat(StatsType.Defence) +
                pokemon.getEVs().getStat(StatsType.SpecialAttack) + pokemon.getEVs().getStat(StatsType.SpecialDefence) + pokemon.getEVs().getStat(StatsType.Speed);
        String totalEVs = df.format((int) (evSum / 510.0 * 100.0)) + "%";
        String totalIVs = df.format((int) (ivSum / 186.0 * 100.0)) + "%";
        String speciesName = pokemon.getSpecies().name();
        String nickname = (pokemon.getNickname() == null) ? pa.getLanguage().getString("pixelmon." + speciesName.toLowerCase() + ".name") : pokemon.getNickname();
        String shiny = AyCore.getInstance().getConfig().getString("text.shiny." + pokemon.isShiny(), "<ERROR>");
        int level = pokemon.getLevel();
        String nature = pa.getLanguage().getString("enum.nature." + pokemon.getNature().name().toLowerCase());
        String growth = pa.getLanguage().getString("enum.growth." + pokemon.getGrowth().name().toLowerCase());
        String ability = pa.getLanguage().getString("ability." + pokemon.getAbility().getName() + ".name");
        String ability_des = pa.getLanguage().getString("ability." + pokemon.getAbility().getName() + ".description"),
                nullOption = AyCore.getInstance().getConfig().getString("text.error", "<ERROR>");
        String originalTrainer = pokemon.getOriginalTrainer() == null ? nullOption : pokemon.getOriginalTrainer();
        for (int i = 0; i < 4; i++) {
            result.put(EnumStats.valueOf("Move" + (i + 1)), (pokemon.getMoveset().get(i) != null) ? (pa.getLanguage().getString("attack." +
                    getAttackBase(pokemon.getMoveset().get(i)).toLowerCase() + ".name")) : nullOption);
        }
        result.put(EnumStats.IVS_HP, pokemon.getIVs().getStat(StatsType.HP));
        result.put(EnumStats.IVS_Attack, pokemon.getIVs().getStat(StatsType.Attack));
        result.put(EnumStats.IVS_Speed, pokemon.getIVs().getStat(StatsType.Speed));
        result.put(EnumStats.IVS_Defence, pokemon.getIVs().getStat(StatsType.Defence));
        result.put(EnumStats.IVS_SpecialAttack, pokemon.getIVs().getStat(StatsType.SpecialAttack));
        result.put(EnumStats.IVS_SpecialDefence, pokemon.getIVs().getStat(StatsType.SpecialDefence));
        result.put(EnumStats.EVS_HP, pokemon.getEVs().getStat(StatsType.HP));
        result.put(EnumStats.EVS_Attack, pokemon.getEVs().getStat(StatsType.Attack));
        result.put(EnumStats.EVS_Speed, pokemon.getEVs().getStat(StatsType.Speed));
        result.put(EnumStats.EVS_Defence, pokemon.getEVs().getStat(StatsType.Defence));
        result.put(EnumStats.EVS_SpecialAttack, pokemon.getEVs().getStat(StatsType.SpecialAttack));
        result.put(EnumStats.EVS_SpecialDefence, pokemon.getEVs().getStat(StatsType.SpecialDefence));
        result.put(EnumStats.Level, level);
        result.put(EnumStats.Owner, pokemon.getOwnerPlayer() != null ? pokemon.getOwnerPlayer().getDisplayNameString() : "无");
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
        result.put(EnumStats.SPECIE_NAME, speciesName);
        result.put(EnumStats.SPEC_FLAG_UNTRADEABLE, AyCore.getInstance().getConfig().getString("text.common." + pokemon.hasSpecFlag("untradeable")));
        result.put(EnumStats.SPEC_FLAG_UNBTREEDABLE, AyCore.getInstance().getConfig().getString("text.common." + pokemon.hasSpecFlag("unbreedable")));
        return result;
    }

    public String getAttackBase(Attack ms) {
        String attackName = "";
        Object object = getField(ms.getClass(), "baseAttack", ms);
        AttackBase ab = (object != null ? (AttackBase) object : null);
        if (ab != null) {
            attackName = (String) getField(ab.getClass(), "attackName", ab);
        }
        return attackName.replace(" ", "_");
    }
}
