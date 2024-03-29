package com.aystudio.core.pixelmon.api.i18n;

import com.google.common.collect.Maps;
import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.util.custom.LoggerUtil;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraft.util.text.translation.LanguageMap;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
 * @author Blank038
 * @since 2022-01-09
 */
public class PixelmonI18n {
    private Map<String, String> values = Maps.newHashMap();
    private final boolean highVersion;

    public PixelmonI18n(AyCore pa, boolean v1_12_2) {
        this.highVersion = v1_12_2;
        try {
            if (highVersion) {
                InputStream inputStream = LanguageMap.class.getResourceAsStream("/assets/pixelmon/lang/" + pa.getConfig().getString("lang") + ".lang");
                values = LanguageMap.parseLangFile(inputStream);
            } else {
                InputStream inputStream = Pixelmon.class.getResourceAsStream("/assets/pixelmon/lang/" + pa.getConfig().getString("lang") + ".lang");
                byte[] data = IOUtils.toByteArray(inputStream);
                Properties props = new Properties();
                props.load(new InputStreamReader(new ByteArrayInputStream(data), StandardCharsets.UTF_8));
                for (Map.Entry<Object, Object> e : props.entrySet()) {
                    values.put((String) e.getKey(), (String) e.getValue());
                }
            }
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
        if (highVersion) {
            return values.getOrDefault(key, "");
        } else {
            return "";
        }
    }
}