package com.aystudio.core.pixelmon.api.i18n;

import com.google.common.collect.Maps;
import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.util.custom.LoggerUtil;
import org.bukkit.ChatColor;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Blank038
 * @since 2022-01-09
 */
public class PixelmonI18n {
    protected final AyCore plugin = AyCore.getInstance();
    protected final Map<String, String> values = Maps.newHashMap();
    protected Consumer<String> initializeConsumer;

    public void initialize() {
        String lang = this.plugin.getConfig().getString("lang");
        try {
            this.initializeConsumer.accept(lang);
            LoggerUtil.getOrRegister(AyCore.class).log("§f成功加载语言文件 " + ChatColor.GREEN + lang + " §f(" + values.size() + "个词条)");
        } catch (Exception e) {
            LoggerUtil.getOrRegister(AyCore.class).log(ChatColor.RED + "读取语言文件 " + ChatColor.WHITE + lang + ChatColor.RED + " 异常, 请检查文件是否正确!");
        }
    }

    public String getString(String key) {
        return values.getOrDefault(key, "");
    }
}