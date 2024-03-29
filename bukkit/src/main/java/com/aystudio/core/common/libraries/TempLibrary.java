package com.aystudio.core.common.libraries;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.util.common.ReflectionUtil;
import com.aystudio.core.bukkit.util.file.LibFileDownload;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;

/**
 * @author Blank038
 */
@Deprecated
public class TempLibrary {

    public static void loadLibraries() {
        // 初始化依赖列表
        JsonObject libJson = initLibList();
        // 开始加载
        File libFolder = new File(AyCore.getInstance().getDataFolder(), "lib");
        libFolder.mkdir();
        // 开始加载 Kotlin 依赖
        if (libJson.has("lib")) {
            JsonObject lib = libJson.getAsJsonObject("lib");
            for (Map.Entry<String, JsonElement> entry : lib.entrySet()) {
                JsonObject target = entry.getValue().getAsJsonObject();
                if (ReflectionUtil.hasClass(target.get("class").getAsString())) {
                    AyCore.getInstance().getConsoleLogger().log("&f依赖 &a" + entry.getKey() + " &f已被加载, 取消检测.");
                    continue;
                }
                File file = new File(libFolder, target.get("file").getAsString());
                String url = target.get("url").getAsString().replace("$lib-name", target.get("file").getAsString());
                if (!file.exists()) {
                    new LibFileDownload(url, file) {
                        @Override
                        public void success() {
                            AyCore.getInstance().getConsoleLogger().log("&f成功下载依赖 &a" + entry.getKey());
                        }

                        @Override
                        public void deny() {
                            AyCore.getInstance().getConsoleLogger().log("&c下载依赖 &f" + entry.getKey() + " &c失败");
                        }
                    }.start();
                } else {
                    boolean result = new LibFileDownload(url, file).load();
                    if (result) {
                        AyCore.getInstance().getConsoleLogger().log("&f成功加载依赖 &a" + entry.getKey());
                    } else {
                        AyCore.getInstance().getConsoleLogger().log("&c加载依赖 &f" + entry.getKey() + " &c失败");
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
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                         IllegalAccessException ignored) {
                }
            }
        }
        // 检测 HikariCP 可否使用
        if (AyCore.getInstance().hasHikariCP()) {
            try {
                Class<?> c1 = Class.forName("com.zaxxer.hikari.HikariConfig");
                Method method = c1.getMethod("copyStateTo", c1);
                AyCore.getInstance().setSameHikariVersion((method != null));
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                AyCore.getInstance().setSameHikariVersion(false);
            }
        }
    }

    private static JsonObject initLibList() {
        try {
            URL url = new URL("https://www.mc9y.com/info/libs.json");
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder json = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                json.append(text);
            }
            is.close();
            JsonObject libJson = AyCore.GSON.fromJson(json.toString(), JsonObject.class);
            AyCore.getInstance().getConsoleLogger().log("&f成功拉取依赖资源下载链接");
            return libJson;
        } catch (Exception ignored) {
            AyCore.getInstance().getConsoleLogger().log("&c依赖资源下载链接拉取失败");
        }
        return new JsonObject();
    }
}
