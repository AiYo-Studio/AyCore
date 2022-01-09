package com.aystudio.core.bukkit;

import com.aystudio.core.bukkit.command.BlankCommand;
import com.aystudio.core.bukkit.command.registry.CommandRegistry;
import com.aystudio.core.bukkit.listener.PluginStatusListener;
import com.aystudio.core.bukkit.plugin.AyPlugin;
import com.aystudio.core.bukkit.thread.ThreadProcessor;
import com.aystudio.core.bukkit.util.common.ReflectionUtil;
import com.aystudio.core.forge.ForgeInject;
import com.google.gson.*;
import com.aystudio.core.bukkit.listener.CommandListener;
import com.aystudio.core.bukkit.model.common.PluginData;
import com.aystudio.core.bukkit.nms.sub.v1_12_R1;
import com.aystudio.core.bukkit.nms.sub.v1_16_R1;
import com.aystudio.core.bukkit.util.custom.VerCheck;
import com.aystudio.core.bukkit.util.file.LibFileDownload;
import com.aystudio.core.bukkit.util.key.KeyChannel;
import com.aystudio.core.pixelmon.PokemonAPI;
import com.aystudio.core.bukkit.nms.INMSClass;
import com.aystudio.core.bukkit.metrics.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Blank038
 * @since 2021-03-16
 */
@SuppressWarnings("unused")
public class AyCore extends AyPlugin {
    private static AyCore ayCore;
    private static PokemonAPI pokemonAPI;
    private static CommandRegistry commandRegistry;
    public static final Gson GSON = new GsonBuilder().create();

    private KeyChannel keyChannel;
    private INMSClass nmsClass;
    private boolean sameHikariVersion;
    public HashMap<Plugin, PluginData> dataMap = new HashMap<>();
    public JsonObject libJson = new JsonObject();

    /**
     * 获得 Blank038API 主类实例
     *
     * @return 主类实例
     */
    public static AyCore getBlank038API() {
        return ayCore;
    }

    /**
     * 获取 PokemonAPI 类对象
     *
     * @return PokemonAPI 对象
     */
    public static PokemonAPI getPokemonAPI() {
        return pokemonAPI;
    }

    /**
     * 获取命令注册器
     *
     * @return 命令注册器
     */
    public static CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

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
        ayCore = this;
        pokemonAPI = new PokemonAPI();
        commandRegistry = new CommandRegistry();
        // 载入配置和注册核心内容
        this.loadConfig();
        // 载入依赖
        this.getConsoleLogger().setPrefix("&f[&eAC&f] - ");
        this.getConsoleLogger().log(false, " ");
        this.getConsoleLogger().log(false, "   &3AyCore &bv" + getDescription().getVersion());
        this.getConsoleLogger().log(false, " ");
        this.getConsoleLogger().log(false, "&f[&eAC&f] &6AyCore //>");
        this.loadLibraries();
        this.getConsoleLogger().log(false, " ");
    }

    @Override
    public void onEnable() {
        // 开始输出
        this.getConsoleLogger().log(false, " ");
        this.getConsoleLogger().log(false, "   &3AyCore &bv" + getDescription().getVersion());
        this.getConsoleLogger().log(false, " ");
        this.getConsoleLogger().log(false, "&f[&eAC&f] &6AyCore //>");
        // 注入 Forge 模块
        new ForgeInject();
        // 初始化 PokemonAPI
        pokemonAPI.onLoad();
        this.init();
        // 统计和更新检测
        new Metrics(this);
        VerCheck check = new VerCheck(this, "http://www.mc9y.com/checks/{plugin}.txt");
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
        this.getDataFolder().mkdir();
        this.saveDefaultConfig();
        this.reloadConfig();
        PokemonAPI.old = getConfig().getBoolean("old");
    }

    public void init() {
        keyChannel = new KeyChannel();
        Bukkit.getPluginManager().registerEvents(new PluginStatusListener(), this);
        Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
        // 检查 nms
        String version = "未知";
        try {
            version = Bukkit.getServer().getClass().getPackage().toString().replace(".", ",").split(",")[3];
        } catch (Exception ignored) {
        }
        switch (version) {
            case "v1_12_R1":
                this.setNMSClass(new v1_12_R1());
                break;
            case "v1_16_R1":
                this.setNMSClass(new v1_16_R1());
                break;
            default:
                this.getConsoleLogger().log("&f挂钩核心NMS: &c无挂钩");
                break;
        }
        this.getConsoleLogger().log("&f成功加载: &aBlank038API");
    }

    public void loadLibraries() {
        // 初始化依赖列表
        this.initLibList();
        // 开始加载
        File libFolder = new File(getDataFolder(), "lib");
        libFolder.mkdir();
        // 开始加载 Kotlin 依赖
        if (libJson.has("lib")) {
            JsonObject lib = libJson.getAsJsonObject("lib");
            for (Map.Entry<String, JsonElement> entry : lib.entrySet()) {
                JsonObject target = entry.getValue().getAsJsonObject();
                if (ReflectionUtil.hasClass(target.get("class").getAsString())) {
                    this.getConsoleLogger().log("&f依赖 &a" + entry.getKey() + " &f已被加载, 取消检测.");
                    continue;
                }
                File file = new File(libFolder, target.get("file").getAsString());
                String url = target.get("url").getAsString().replace("$lib-name", target.get("file").getAsString());
                if (!file.exists()) {
                    new LibFileDownload(url, file) {
                        @Override
                        public void success() {
                            AyCore.this.getConsoleLogger().log("&f成功下载依赖 &a" + entry.getKey());
                        }

                        @Override
                        public void deny() {
                            AyCore.this.getConsoleLogger().log("&c下载依赖 &f" + entry.getKey() + " &c失败");
                        }
                    }.start();
                } else {
                    boolean result = new LibFileDownload(url, file).load();
                    if (result) {
                        this.getConsoleLogger().log("&f成功加载依赖 &a" + entry.getKey());
                    } else {
                        this.getConsoleLogger().log("&c加载依赖 &f" + entry.getKey() + " &c失败");
                    }
                }
            }
        }
        if (libJson.has("load")) {
            JsonArray lib = libJson.getAsJsonArray("load");
            for (int i = 0; i < lib.size(); i++) {
                try {
                    String target = lib.get(i).getAsString();
                    Class<?> c = Class.forName(target);
                    Method method = c.getMethod("onEnable");
                    method.invoke(c);
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
                }
            }
        }
        // 检测 HikariCP 可否使用
        if (this.hasHikariCP()) {
            try {
                Class<?> c1 = Class.forName("com.zaxxer.hikari.HikariConfig");
                Method method = c1.getMethod("copyStateTo", c1);
                this.sameHikariVersion = (method != null);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                this.sameHikariVersion = false;
            }
        }
    }

    /**
     * 获取按键监听通道
     *
     * @return 按键监听通道
     */
    public KeyChannel getKeyChannel() {
        return keyChannel;
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

    public boolean isSameHikariVersion() {
        return this.sameHikariVersion;
    }

    private void setNMSClass(INMSClass nmsClass) {
        this.nmsClass = nmsClass;
        nmsClass.registerChannel(this);
        this.getConsoleLogger().log("&f成功加载: &a" + nmsClass.getVID());
    }

    private void initLibList() {
        try {
            URL url = new URL("http://www.mc9y.com/info/libs.json");
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder json = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                json.append(text);
            }
            is.close();
            libJson = AyCore.GSON.fromJson(json.toString(), JsonObject.class);
            this.getConsoleLogger().log("&f成功拉取依赖资源下载链接");
        } catch (Exception ignored) {
            this.getConsoleLogger().log("&c依赖资源下载链接拉取失败");
        }
    }
}