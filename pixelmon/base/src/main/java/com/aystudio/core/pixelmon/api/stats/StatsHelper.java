package com.aystudio.core.pixelmon.api.stats;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.pixelmon.PokemonAPI;
import com.aystudio.core.pixelmon.api.enums.EnumStats;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class StatsHelper {
    protected final PokemonAPI pa = PokemonAPI.getInstance();

    protected Map<EnumStats, Object> getStats(Pokemon pokemon) {
        return new HashMap<>();
    }

    public String format(Pokemon pokemon, String text) {
        String formatText = text;
        if (text.contains("%")) {
            Map<EnumStats, Object> hashmap = getStats(pokemon);
            String rt = split(formatText, hashmap);
            if (rt != null) {
                formatText = rt;
            }
        }
        return formatText.replace("&", "§");
    }

    public List<String> format(Pokemon pokemon, List<String> list) {
        List<String> list1 = new ArrayList<>();
        Map<EnumStats, Object> hashmap = getStats(pokemon);
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

    public String split(String text, Map<EnumStats, Object> hashmap) {
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
            AyCore.getInstance().getLogger().log(Level.SEVERE, e, () -> "反射获取字段值出现错误 " + c.getSimpleName() + "," + fieldName + "," + src.toString());
        }
        return object;
    }
}