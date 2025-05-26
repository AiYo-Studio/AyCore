package com.aystudio.core.bukkit;

import com.aystudio.core.bukkit.command.BlankCommand;
import com.aystudio.core.bukkit.command.registry.CommandRegistry;
import com.aystudio.core.bukkit.cron.CronScheduler;
import com.aystudio.core.bukkit.handler.PlatformHandler;
import com.aystudio.core.bukkit.listener.PluginStatusListener;
import com.aystudio.core.bukkit.platform.IPlatformApi;
import com.aystudio.core.bukkit.plugin.AyPlugin;
import com.aystudio.core.bukkit.thread.ThreadProcessor;
import com.aystudio.core.bukkit.util.mixed.PixelmonUtil;
import com.aystudio.core.common.data.CommonData;
import com.aystudio.core.common.libraries.TempLibrary;
import com.aystudio.core.common.libraries.loader.PixelmonLibraryLoader;
import com.aystudio.core.common.link.ILink;
import com.google.gson.*;
import com.aystudio.core.bukkit.listener.CommandListener;
import com.aystudio.core.bukkit.model.common.PluginData;
import com.aystudio.core.bukkit.nms.sub.v1_12_R1;
import com.aystudio.core.bukkit.nms.sub.v1_16_R1;
import com.aystudio.core.bukkit.util.custom.VerCheck;
import com.aystudio.core.bukkit.util.key.KeyChannel;
import com.aystudio.core.bukkit.nms.INMSClass;
import com.aystudio.core.bukkit.metrics.Metrics;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Blank038
 * @since 2021-03-16
 */
@Getter
@Setter
public class AyCore extends AyPlugin {
    @Getter
    private static AyCore instance;
    @Getter
    @Setter
    private static IPlatformApi platformApi;
    @Getter
    private static CommandRegistry commandRegistry;
    public static final Gson GSON = new GsonBuilder().create();

    private ILink pokemonApi;
    private KeyChannel keyChannel;
    private INMSClass nmsClass;
    private boolean sameHikariVersion;
    public Map<Plugin, PluginData> dataMap = new HashMap<>();

    /**
     * 获取 NMS 接口对象
     *
     * @return NMS 接口
     */
    public INMSClass getNMSClass() {
        return nmsClass;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        // 开始初始化
        instance = this;
        // 载入配置和注册核心内容
        this.loadConfig();
        // 初始化平台
        PlatformHandler.initPlatform();
        // 载入宝可梦模块(临时使用)
        PixelmonUtil.checkAndRun(() -> {
            // TODO: 等待重构
            new PixelmonLibraryLoader().pull();
            // 初始化 PokemonAPI
            pokemonApi = ILink.newLink("com.aystudio.core.pixelmon.PokemonAPI");
        }, null);
        commandRegistry = new CommandRegistry();
        // 载入依赖
        this.getConsoleLogger().setPrefix("&f[&eAC&f] - ");
        this.getConsoleLogger().log(false, " ");
        this.getConsoleLogger().log(false, "   &3AyCore &bv" + getDescription().getVersion());
        this.getConsoleLogger().log(false, " ");
        this.getConsoleLogger().log(false, "&f[&eAC&f] &6AyCore //>");
        // TODO: Code that will be deleted in the future
        TempLibrary.loadLibraries();
        this.getConsoleLogger().log(false, " ");

        CronScheduler scheduler = CronScheduler.create(this);
        scheduler.createJob("test", "test", "* * * * * ?", (context) -> {
            System.out.println(System.currentTimeMillis());
        });
    }

    @Override
    public void onEnable() {
        // 开始输出
        this.getConsoleLogger().log(false, " ");
        this.getConsoleLogger().log(false, "   &3AyCore &bv" + getDescription().getVersion());
        this.getConsoleLogger().log(false, " ");
        this.getConsoleLogger().log(false, "&f[&eAC&f] &6AyCore //>");
        // 注入 Forge 模块
        ILink.newLink("com.aystudio.core.forge.ForgeInject").onLoad();
        // 初始化内部方法
        this.init();
        // 初始化 PokemonAPI
        if (pokemonApi != null) {
            pokemonApi.onLoad();
        }
        // 检测 NMS 版本
        if (this.nmsClass == null) {
            this.getConsoleLogger().log("&f挂钩核心NMS: &c无挂钩");
        } else {
            this.getConsoleLogger().log("&f成功加载: &a" + nmsClass.getVID());
        }
        // 统计和更新检测
        new Metrics(this);
        VerCheck check = new VerCheck(this, "https://www.mc9y.com/checks/{plugin}.txt");
        if (check.isError()) {
            this.getConsoleLogger().log("&f检测新版本异常, 不影响使用");
        } else {
            if (check.isIdentical()) {
                this.getConsoleLogger().log("&f插件已是最新版, 无需更新");
            } else {
                this.getConsoleLogger().log("&f插件可更新, 最新版: &e" + check.getVersion());
            }
        }
        commandRegistry.registerCommand(this, new Object[]{new BlankCommand()}, new String[]{"aycore"});
        // 开始监听
        this.getConsoleLogger().log("&f插件加载完成, 感谢使用!");
        this.getConsoleLogger().log(false, " ");
    }

    /**
     * 插件关闭时
     */
    @Override
    public void onDisable() {
        ThreadProcessor.stopAll();
    }

    /**
     * 载入插件配置文件
     */
    public void loadConfig() {
        this.saveDefaultConfig();
        this.reloadConfig();
        CommonData.debug = this.getConfig().getBoolean("debug");
    }

    public void init() {
        keyChannel = new KeyChannel();
        Bukkit.getPluginManager().registerEvents(new PluginStatusListener(), this);
        Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
        if (this.nmsClass == null) {
            switch (CommonData.coreVersion) {
                case "v1_12_R1":
                    this.setNMSClass(new v1_12_R1());
                    break;
                case "v1_16_R1":
                    this.setNMSClass(new v1_16_R1());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 判断是否加载了连接池 HikariCP
     *
     * @return 是否加载了连接池 HikariCP
     */
    public boolean hasHikariCP() {
        try {
            Class.forName("com.zaxxer.hikari.HikariConfig", false, this.getClassLoader());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setNMSClass(INMSClass nmsClass) {
        this.nmsClass = nmsClass;
        nmsClass.registerChannel(this);
    }
}