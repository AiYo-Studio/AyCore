package com.aystudio.core.pixelmon.api.stats;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.pixelmon.PokemonAPI;
import com.aystudio.core.pixelmon.api.enums.EnumStats;
import com.aystudio.core.pixelmon.api.pokemon.PokemonUtil;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

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
        int ivSum = pokemon.getIVs().getStat(StatsType.HP) + pokemon.getIVs().getStat(StatsType.Attack) + pokemon.getIVs().getStat(StatsType.Defence) +
                pokemon.getIVs().getStat(StatsType.SpecialAttack) + pokemon.getIVs().getStat(StatsType.SpecialDefence) + pokemon.getIVs().getStat(StatsType.Speed);
        int evSum = pokemon.getEVs().getStat(StatsType.HP) + pokemon.getEVs().getStat(StatsType.Attack) + pokemon.getEVs().getStat(StatsType.Defence) +
                pokemon.getEVs().getStat(StatsType.SpecialAttack) + pokemon.getEVs().getStat(StatsType.SpecialDefence) + pokemon.getEVs().getStat(StatsType.Speed);
        String totalEVs = df.format((int) (evSum / 510.0 * 100.0)) + "%";
        String totalIVs = df.format((int) (ivSum / 186.0 * 100.0)) + "%";
        String nickname = (pokemon.getNickname() == null) ? pa.getLanguage().getString("pixelmon."
                + pokemon.getSpecies().getPokemonName().toLowerCase() + ".name") : pokemon.getNickname();
        String shiny = AyCore.getInstance().getConfig().getString("text.shiny." + pokemon.isShiny(), "<ERROR>");
        int level = pokemon.getLevel();
        String nature = pa.getLanguage().getString("enum.nature." + pokemon.getNature().name().toLowerCase());
        String growth = pa.getLanguage().getString("enum.growth." + pokemon.getGrowth().name().toLowerCase());
        String ability = pa.getLanguage().getString("ability." + pokemon.getAbility().getName() + ".name");
        String ability_des = pa.getLanguage().getString("ability." + pokemon.getAbility().getName() + ".description"),
                nullOption = AyCore.getInstance().getConfig().getString("text.error", "<ERROR>");
        String originalTrainer = pokemon.getOriginalTrainer() == null ? nullOption : pokemon.getOriginalTrainer();

        /*
            开始计算技能
         */
        for (int i = 0; i < 4; i++) {
            pshm.put(EnumStats.valueOf("Move" + (i + 1)), (pokemon.getMoveset().get(i) != null) ? (pa.getLanguage().getString("attack." +
                    getAttackBase(pokemon.getMoveset().get(i)).toLowerCase() + ".name")) : nullOption);
        }

        pshm.put(EnumStats.IVS_HP, pokemon.getIVs().getStat(StatsType.HP));
        pshm.put(EnumStats.IVS_Attack, pokemon.getIVs().getStat(StatsType.Attack));
        pshm.put(EnumStats.IVS_Speed, pokemon.getIVs().getStat(StatsType.Speed));
        pshm.put(EnumStats.IVS_Defence, pokemon.getIVs().getStat(StatsType.Defence));
        pshm.put(EnumStats.IVS_SpecialAttack, pokemon.getIVs().getStat(StatsType.SpecialAttack));
        pshm.put(EnumStats.IVS_SpecialDefence, pokemon.getIVs().getStat(StatsType.SpecialDefence));
        pshm.put(EnumStats.EVS_HP, pokemon.getEVs().getStat(StatsType.HP));
        pshm.put(EnumStats.EVS_Attack, pokemon.getEVs().getStat(StatsType.Attack));
        pshm.put(EnumStats.EVS_Speed, pokemon.getEVs().getStat(StatsType.Speed));
        pshm.put(EnumStats.EVS_Defence, pokemon.getEVs().getStat(StatsType.Defence));
        pshm.put(EnumStats.EVS_SpecialAttack, pokemon.getEVs().getStat(StatsType.SpecialAttack));
        pshm.put(EnumStats.EVS_SpecialDefence, pokemon.getEVs().getStat(StatsType.SpecialDefence));
        pshm.put(EnumStats.Level, level);
        pshm.put(EnumStats.Owner, pokemon.getOwnerPlayer() != null ? pokemon.getOwnerPlayer().getDisplayNameString() : "无");
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
        pshm.put(EnumStats.SPECIE_NAME, pokemon.getSpecies().name());
        pshm.put(EnumStats.SPEC_FLAG_UNTRADEABLE, AyCore.getInstance().getConfig().getString("text.common." + pokemon.hasSpecFlag("untradeable")));
        pshm.put(EnumStats.SPEC_FLAG_UNBTREEDABLE, AyCore.getInstance().getConfig().getString("text.common." + pokemon.hasSpecFlag("unbreedable")));
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
        AttackBase ab = (object != null ? (AttackBase) object : null);
        if (ab != null) {
            attackName = (String) getField(ab.getClass(), "attackName", ab);
        }
        return PokemonAPI.old ? attackName : attackName.replace(" ", "_");
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