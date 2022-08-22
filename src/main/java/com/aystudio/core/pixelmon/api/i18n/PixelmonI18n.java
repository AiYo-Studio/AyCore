package com.aystudio.core.pixelmon.api.i18n;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.util.custom.LoggerUtil;
import net.minecraft.util.text.LanguageMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author Blank038
 * @since 2022-01-09
 */
public class PixelmonI18n {
    private Map<String, String> values = Maps.newHashMap();

    public PixelmonI18n(AyCore pa) {
        try {
//            if (highVersion) {
//                InputStream inputStream = LanguageMap.class.getResourceAsStream("/assets/pixelmon/lang/" + pa.getConfig().getString("lang") + ".lang");
//                values = LanguageMap(inputStream);
//            } else {
//                InputStream inputStream = Pixelmon.class.getResourceAsStream("/assets/pixelmon/lang/" + pa.getConfig().getString("lang") + ".lang");
//                byte[] data = IOUtils.toByteArray(inputStream);
//                Properties props = new Properties();
//                props.load(new InputStreamReader(new ByteArrayInputStream(data), StandardCharsets.UTF_8));
//                for (Map.Entry<Object, Object> e : props.entrySet()) {
//                    values.put((String) e.getKey(), (String) e.getValue());
//                }
//            }
            InputStream inputStream = LanguageMap.class.getResourceAsStream("/assets/pixelmon/lang/" + pa.getConfig().getString("lang") + ".json");
            ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            BiConsumer<String, String> biconsumer = builder::put;
            LanguageMap.loadFromJson(inputStream, biconsumer);
            this.values = new HashMap<>(builder.build());
            LoggerUtil.getOrRegister(AyCore.class).log("§f成功加载语言文件 " + ChatColor.GREEN + pa.getConfig().getString("lang")
                    + " §f(" + values.size() + "个词条)");
        } catch (Exception e) {
            LoggerUtil.getOrRegister(AyCore.class).log("§f" + ChatColor.RED + "读取语言文件 " + ChatColor.WHITE + pa.getConfig().getString("lang")
                    + ChatColor.RED + " 异常, 请检查文件是否正确!");
            Bukkit.getPluginManager().disablePlugin(pa);
        }
    }

    /**
     * 获取语言文件内对应的内容
     * 例如获取 妙蛙种子 的语言译名
     *
     * @param key 应该为 pixelmon.bulbasaur.name
     * @return 词条
     */
    public String getString(String key) {
        return values.getOrDefault(key, "");
    }
}