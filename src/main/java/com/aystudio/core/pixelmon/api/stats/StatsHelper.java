package com.aystudio.core.pixelmon.api.stats;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.pixelmon.PokemonAPI;
import com.aystudio.core.pixelmon.api.enums.EnumStats;
import com.aystudio.core.pixelmon.api.pokemon.PokemonUtil;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.ImmutableAttack;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatsHelper {
    protected final PokemonAPI pa;

    public StatsHelper(PokemonAPI pa) {
        this.pa = pa;
    }

    public HashMap<EnumStats, Object> getPokeStats(Pokemon pokemon) {
        HashMap<EnumStats, Object> pshm = new HashMap<>();
        DecimalFormat df = new DecimalFormat("#0.##");
        int ivSum = pokemon.getIVs().getStat(BattleStatsType.HP) + pokemon.getIVs().getStat(BattleStatsType.ATTACK) + pokemon.getIVs().getStat(BattleStatsType.DEFENSE) +
                pokemon.getIVs().getStat(BattleStatsType.SPECIAL_ATTACK) + pokemon.getIVs().getStat(BattleStatsType.SPECIAL_DEFENSE) + pokemon.getIVs().getStat(BattleStatsType.SPEED);
        int evSum = pokemon.getEVs().getStat(BattleStatsType.HP) + pokemon.getEVs().getStat(BattleStatsType.ATTACK) + pokemon.getEVs().getStat(BattleStatsType.DEFENSE) +
                pokemon.getEVs().getStat(BattleStatsType.SPECIAL_ATTACK) + pokemon.getEVs().getStat(BattleStatsType.SPECIAL_DEFENSE) + pokemon.getEVs().getStat(BattleStatsType.SPEED);
        String totalEVs = df.format((int) (evSum / 510.0 * 100.0)) + "%";
        String totalIVs = df.format((int) (ivSum / 186.0 * 100.0)) + "%";
        String nickname = (pokemon.getNickname() == null) ? PokemonUtil.getPokemonName(pokemon.getSpecies()) : pokemon.getNickname();
        String shiny = AyCore.getInstance().getConfig().getString("text.shiny." + pokemon.isShiny(), "<ERROR>");
        int level = pokemon.getPokemonLevel();
        String nature = pa.getLanguage().getString(pokemon.getNature().getTranslationKey());
        String growth = pa.getLanguage().getString(pokemon.getGrowth().getTranslationKey());
        String ability = pa.getLanguage().getString(pokemon.getAbility().getTranslationKey());
        String ability_des = pa.getLanguage().getString(pokemon.getAbility().getTranslationKey() + ".description"),
                nullOption = AyCore.getInstance().getConfig().getString("text.error", "<ERROR>");
        String originalTrainer = pokemon.getOriginalTrainer() == null ? nullOption : pokemon.getOriginalTrainer();

        /*
            开始计算技能
         */
        for (int i = 0; i < 4; i++) {
            pshm.put(EnumStats.valueOf("Move" + (i + 1)), (pokemon.getMoveset().get(i) != null) ?
                    (pa.getLanguage().getString(this.getAttackBase(pokemon.getMoveset().get(i)).toLowerCase())) : nullOption);
        }

        pshm.put(EnumStats.IVS_HP, pokemon.getIVs().getStat(BattleStatsType.HP));
        pshm.put(EnumStats.IVS_Attack, pokemon.getIVs().getStat(BattleStatsType.ATTACK));
        pshm.put(EnumStats.IVS_Speed, pokemon.getIVs().getStat(BattleStatsType.SPEED));
        pshm.put(EnumStats.IVS_Defence, pokemon.getIVs().getStat(BattleStatsType.DEFENSE));
        pshm.put(EnumStats.IVS_SpecialAttack, pokemon.getIVs().getStat(BattleStatsType.SPECIAL_ATTACK));
        pshm.put(EnumStats.IVS_SpecialDefence, pokemon.getIVs().getStat(BattleStatsType.SPECIAL_DEFENSE));
        pshm.put(EnumStats.EVS_HP, pokemon.getEVs().getStat(BattleStatsType.HP));
        pshm.put(EnumStats.EVS_Attack, pokemon.getEVs().getStat(BattleStatsType.ATTACK));
        pshm.put(EnumStats.EVS_Speed, pokemon.getEVs().getStat(BattleStatsType.SPEED));
        pshm.put(EnumStats.EVS_Defence, pokemon.getEVs().getStat(BattleStatsType.DEFENSE));
        pshm.put(EnumStats.EVS_SpecialAttack, pokemon.getEVs().getStat(BattleStatsType.SPECIAL_ATTACK));
        pshm.put(EnumStats.EVS_SpecialDefence, pokemon.getEVs().getStat(BattleStatsType.SPECIAL_DEFENSE));
        pshm.put(EnumStats.Level, level);
        pshm.put(EnumStats.Owner, pokemon.getOwnerPlayer() != null ? pokemon.getOwnerPlayer().getName().getString() : "无");
        pshm.put(EnumStats.Shiny, shiny);
        pshm.put(EnumStats.Growth, growth);
        String gender = AyCore.getInstance().getConfig().getString("text.gender." + pokemon.getGender().name().toLowerCase());
        pshm.put(EnumStats.Gender, gender);
        pshm.put(EnumStats.Nature, nature);
        pshm.put(EnumStats.IVS_SUM, totalIVs);
        pshm.put(EnumStats.EVS_SUM, totalEVs);
        pshm.put(EnumStats.Nick_Name, nickname);
        pshm.put(EnumStats.Ability, ability);
        pshm.put(EnumStats.Ability_DES, ability_des);
        pshm.put(EnumStats.OriginalTrainer, originalTrainer);
        pshm.put(EnumStats.TRANSLATE_NAME, PokemonUtil.getPokemonName(pokemon.getSpecies()));
        pshm.put(EnumStats.SPECIE_NAME, pokemon.getSpecies().getName());
        pshm.put(EnumStats.SPEC_FLAG_UNTRADEABLE, AyCore.getInstance().getConfig().getString("text.common." + pokemon.hasFlag("untradeable")));
        pshm.put(EnumStats.SPEC_FLAG_UNBTREEDABLE, AyCore.getInstance().getConfig().getString("text.common." + pokemon.hasFlag("unbreedable")));
        return pshm;
    }

    public String format(Pokemon pokemon, String text) {
        String formatText = text;
        if (text.contains("%")) {
            HashMap<EnumStats, Object> hashmap = getPokeStats(pokemon);
            String rt = split(formatText, hashmap);
            if (rt != null) {
                formatText = rt;
            }
        }
        return formatText.replace("&", "§");
    }

    public List<String> format(Pokemon pokemon, List<String> list) {
        List<String> list1 = new ArrayList<>();
        HashMap<EnumStats, Object> hashmap = getPokeStats(pokemon);
        for (String formatText : list) {
            if (formatText.contains("%")) {
                String rt = split(formatText, hashmap);
                if (rt != null) {
                    formatText = rt;
                }
            }
            list1.add(formatText.replace("&", "§"));
        }
        return list1;
    }

    public String split(String text, HashMap<EnumStats, Object> hashmap) {
        String rt = text;
        String[] fg = text.split("%");
        for (int i = 0; i < fg.length; i++) {
            if (i % 2 != 0) {
                String key = "%" + fg[i] + "%";
                if (EnumStats.has(fg[i])) {
                    EnumStats es = EnumStats.valueOf(fg[i]);
                    rt = rt.replace(key, String.valueOf(hashmap.get(es)));
                }
            }
        }
        return rt;
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

    public Object getField(Class<?> c, String fieldName, Object src) {
        Field[] fields = c.getDeclaredFields();
        Object object = null;
        try {
            for (Field field : fields) {
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                if (field.getName().equals(fieldName)) {
                    object = field.get(src);
                    field.setAccessible(flag);
                    break;
                }
                field.setAccessible(flag);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }
}