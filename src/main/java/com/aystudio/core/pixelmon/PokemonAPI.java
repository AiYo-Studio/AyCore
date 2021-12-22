package com.aystudio.core.pixelmon;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.util.custom.LoggerUtil;
import com.aystudio.core.pixelmon.api.enums.EnumPixelmon;
import com.aystudio.core.pixelmon.api.lang.Lang;
import com.aystudio.core.pixelmon.api.sprite.SpriteHelper;
import com.aystudio.core.pixelmon.api.stats.StatsHelper;
import com.aystudio.core.pixelmon.listener.CatServerModel;
import com.aystudio.core.pixelmon.listener.IForgeListener;
import com.aystudio.core.pixelmon.listener.MagmaModel;
import com.aystudio.core.pixelmon.listener.MohistModel;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;

/**
 * @author Blank038
 */
@SuppressWarnings("unused")
public class PokemonAPI {
    public static boolean old;
    private IForgeListener forgeListener;
    private Lang lang;
    private SpriteHelper sh;
    private StatsHelper statsHelper;
    private EnumPixelmon enumPixelmon;

    @Deprecated
    public static PokemonAPI getInstance() {
        return AyCore.getPokemonAPI();
    }

    public void onLoad() {
        this.inject();
        switch ((enumPixelmon = getPixelmonType())) {
            case PIXELMON_REFORGED:
                this.setupLang();
                statsHelper = new StatsHelper(this);
                sh = new SpriteHelper();
                LoggerUtil.getOrRegister(AyCore.class).log("&fPixelmonReforged 版本: &a" + getVersion(EnumPixelmon.PIXELMON_REFORGED));
                LoggerUtil.getOrRegister(AyCore.class).log("&f成功加载: &aPokemonAPI");
                break;
            case PIXELMON_GENERATIONS:
                this.setupLang();
                LoggerUtil.getOrRegister(AyCore.class).log("&fPixelmonGenerations 版本: &a" + getVersion(EnumPixelmon.PIXELMON_GENERATIONS));
                LoggerUtil.getOrRegister(AyCore.class).log("&f成功加载: &aPokemonAPI");
                break;
            case NONE:
            default:
                break;
        }
    }

    /**
     * 获取是否为宝可梦环境加载
     *
     * @return 是否为宝可梦环境
     */
    @Deprecated
    public boolean isPixelmon() {
        return enumPixelmon != EnumPixelmon.NONE;
    }

    /**
     * 获得 Pixelmon 语言类
     *
     * @return 语言类
     */
    public Lang getLang() {
        return lang;
    }

    /**
     * 获取相片辅助类
     *
     * @return 相片辅助类
     */
    public SpriteHelper getSpriteHelper() {
        return sh;
    }

    public IForgeListener getForgeListener() {
        return this.forgeListener;
    }

    /**
     * 获取当前 Pixelmon 目标类型
     * 仅会检测: PixelmonReforged(重铸), PixelmonGenerations(世代), None(无)
     *
     * @return Pixelmon 目标类型
     */
    public EnumPixelmon getEnumPixelmon() {
        return enumPixelmon;
    }

    private EnumPixelmon getPixelmonType() {
        if (hasClass("com.pixelmonmod.pixelmon.Pixelmon")) {
            return EnumPixelmon.PIXELMON_REFORGED;
        } else if (hasClass("com.pixelmongenerations.core.Pixelmon")) {
            return EnumPixelmon.PIXELMON_GENERATIONS;
        }
        return EnumPixelmon.NONE;
    }

    public void inject() {
        boolean forward = AyCore.getBlank038API().getConfig().getBoolean("forward_forge_event", true);
        if (forward && this.hasClass("catserver.api.bukkit.event.ForgeEvent")) {
            Bukkit.getPluginManager().registerEvents((forgeListener = new CatServerModel()), AyCore.getBlank038API());
            LoggerUtil.getOrRegister(AyCore.class).log("&f载入挂钩核心: §aCatServer");
        } else if (forward && this.hasClass("red.mohist.api.event.BukkitHookForgeEvent")) {
            Bukkit.getPluginManager().registerEvents((forgeListener = new MohistModel()), AyCore.getBlank038API());
            LoggerUtil.getOrRegister(AyCore.class).log("&f载入挂钩核心: §aMohist");
        } else if (forward && this.hasClass("org.magmafoundation.magma.api.events.ForgeEvents")) {
            Bukkit.getPluginManager().registerEvents((forgeListener = new MagmaModel()), AyCore.getBlank038API());
            LoggerUtil.getOrRegister(AyCore.class).log("&f载入挂钩核心: §aMagma");
        } else {
            LoggerUtil.getOrRegister(AyCore.class).log("&f无挂钩核心载入");
        }
    }

    /**
     * 获取 Pokemon 信息处理类
     *
     * @return Pokemon 信息处理类
     */
    public StatsHelper getStatsHelper() {
        return statsHelper;
    }

    public void setupLang() {
        String version = "未知";
        try {
            version = Bukkit.getServer().getClass().getPackage().toString().replace(".", ",").split(",")[3];
        } catch (Exception ignored) {
        }
        switch (version) {
            case "v1_7_R4":
                lang = new Lang(AyCore.getBlank038API(), false);
                break;
            case "v1_12_R1":
                lang = new Lang(AyCore.getBlank038API(), true);
                break;
            default:
                break;
        }
    }

    public String getVersion(EnumPixelmon enumPixelmon) {
        String version = "获取失败";
        try {
            Class<?> c = Class.forName(enumPixelmon.getPackage());
            Field field = c.getField("VERSION");
            version = (String) field.get(c);
            return version;
        } catch (Exception ignored) {
        }
        return version;
    }

    public boolean hasClass(String c) {
        try {
            Class.forName(c);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}