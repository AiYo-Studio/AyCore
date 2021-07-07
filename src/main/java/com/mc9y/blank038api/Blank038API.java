package com.mc9y.blank038api;

import com.mc9y.blank038api.command.BlankCommand;
import com.mc9y.blank038api.command.registry.CommandRegistry;
import com.mc9y.blank038api.listener.PluginStatusListener;
import com.mc9y.blank038api.model.PluginData;
import com.mc9y.blank038api.nms.sub.v1_12_R1;
import com.mc9y.blank038api.nms.sub.v1_16_R1;
import com.mc9y.blank038api.thread.ThreadProcessor;
import com.mc9y.blank038api.util.custom.LoggerUtil;
import com.mc9y.blank038api.util.custom.VerCheck;
import com.mc9y.blank038api.util.file.LibFileDownload;
import com.mc9y.blank038api.util.key.KeyChannel;
import com.mc9y.pokemonapi.PokemonAPI;
import com.mc9y.blank038api.nms.INMSClass;
import com.mc9y.pokemonapi.metrics.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;

/**
 * @author Blank038
 * @since 2021-03-16
 */
@SuppressWarnings("unused")
public class Blank038API extends JavaPlugin {
    private static Blank038API blank038API;
    private static PokemonAPI pokemonAPI;
    private static CommandRegistry commandRegistry;

    private LoggerUtil loggerUtil;
    private KeyChannel keyChannel;
    private INMSClass nmsClass;
    public HashMap<Plugin, PluginData> dataMap = new HashMap<>();
    public JSONObject libJson = new JSONObject();

    /**
     * 获得 Blank038API 主类实例
     *
     * @return 主类实例
     */
    public static Blank038API getBlank038API() {
        return blank038API;
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
        blank038API = this;
        pokemonAPI = new PokemonAPI();
        commandRegistry = new CommandRegistry();
        // 载入配置和注册核心内容
        this.loadConfig();
        // 载入依赖
        this.loggerUtil = LoggerUtil.getOrRegister(Blank038API.class, "&f[&eBAPI&f] - ");
        this.loggerUtil.log(false, " ");
        this.loggerUtil.log(false, "   &3Blank038API &bv" + getDescription().getVersion());
        this.loggerUtil.log(false, " ");
        this.loggerUtil.log(false, "&f[&eBAPI&f] &6Blank038API //>");
        this.loadLibraries();
        this.loggerUtil.log(false, " ");
    }

    @Override
    public void onEnable() {
        // 开始输出
        this.loggerUtil.log(false, " ");
        this.loggerUtil.log(false, "   &3Blank038API &bv" + getDescription().getVersion());
        this.loggerUtil.log(false, " ");
        this.loggerUtil.log(false, "&f[&eBAPI&f] &6Blank038API //>");
        // 初始化 PokemonAPI
        pokemonAPI.onLoad();
        this.init();
        // 统计和更新检测
        new Metrics(this);
        VerCheck check = new VerCheck(this, "http://www.mc9y.com/checks/{plugin}.txt");
        if (check.isError()) {
            this.loggerUtil.log("&f检测新版本异常, 不影响使用");
        } else {
            if (check.isIdentical()) {
                this.loggerUtil.log("&f插件已是最新版, 无需更新");
            } else {
                this.loggerUtil.log("&f插件可更新, 最新版: &e" + check.getVersion());
            }
        }
        commandRegistry.registerCommand(this, new Object[]{new BlankCommand()}, new String[]{"nyapi"});
        // 开始监听
        this.loggerUtil.log("&f插件加载完成, 感谢使用!");
        this.loggerUtil.log(false, " ");
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
                LoggerUtil.getOrRegister(Blank038API.class).log("&f挂钩核心NMS: &c无挂钩");
                break;
        }
        LoggerUtil.getOrRegister(Blank038API.class).log("&f成功加载: &aBlank038API");
    }

    public void loadLibraries() {
        // 初始化依赖列表
        this.initLibList();
        // 开始加载
        File libFolder = new File(getDataFolder(), "lib");
        libFolder.mkdir();
        // 开始加载 Kotlin 依赖
        if (libJson.has("lib")) {
            JSONObject lib = libJson.getJSONObject("lib");
            for (String name : lib.keySet()) {
                JSONObject target = lib.getJSONObject(name);
                if (pokemonAPI.hasClass(target.getString("class"))) {
                    LoggerUtil.getOrRegister(Blank038API.class).log("&f依赖 &a" + name + " &f已被加载, 取消检测.");
                    continue;
                }
                File file = new File(libFolder, target.getString("file"));
                String url = target.getString("url").replace("$lib-name", target.getString("file"));
                if (!file.exists()) {
                    new LibFileDownload(url, file) {
                        @Override
                        public void success() {
                            LoggerUtil.getOrRegister(Blank038API.class).log("&f成功下载依赖 &a" + name);
                        }

                        @Override
                        public void deny() {
                            LoggerUtil.getOrRegister(Blank038API.class).log("&c下载依赖 &f" + name + " &c失败");
                        }
                    }.start();
                } else {
                    new LibFileDownload(url, file).load();
                    LoggerUtil.getOrRegister(Blank038API.class).log("&f成功加载依赖 &a" + name);
                }
            }
        }
        if (libJson.has("load")) {
            JSONArray lib = libJson.getJSONArray("load");
            for (int i = 0; i < lib.length(); i++) {
                try {
                    String target = lib.getString(i);
                    Class<?> c = Class.forName(target);
                    Method method = c.getMethod("onEnable");
                    method.invoke(c);
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
                }
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

    private void setNMSClass(INMSClass nmsClass) {
        this.nmsClass = nmsClass;
        nmsClass.registerChannel(this);
        LoggerUtil.getOrRegister(Blank038API.class).log("&f成功加载: &a" + nmsClass.getVID());
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
            libJson = new JSONObject(json.toString());
            LoggerUtil.getOrRegister(Blank038API.class).log("&f成功拉取依赖资源下载链接");
        } catch (Exception ignored) {
            LoggerUtil.getOrRegister(Blank038API.class).log("&c依赖资源下载链接拉取失败");
        }
    }

}