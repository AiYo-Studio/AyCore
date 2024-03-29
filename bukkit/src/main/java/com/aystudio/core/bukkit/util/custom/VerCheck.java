package com.aystudio.core.bukkit.util.custom;

import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * 目标插件版本检测
 *
 * @author Blank038
 * @since 2021-05-13
 */
public class VerCheck {
    private final String currentVersion;
    private String version;
    private boolean error;

    public VerCheck(Plugin plugin, String url) {
        this.currentVersion = plugin.getDescription().getVersion();
        try {
            URL urlObject = new URL(url.replace("{plugin}", plugin.getDescription().getName()));
            InputStream is = urlObject.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String gg;
            while ((gg = br.readLine()) != null) {
                version = gg;
            }
            br.close();
            isr.close();
            is.close();
        } catch (Exception ignored) {
            this.error = true;
        }
    }

    public String getVersion() {
        return this.version.isEmpty() ? "null" : this.version;
    }

    /**
     * 判断版本是否相同
     *
     * @return 匹配结果
     */
    public boolean isIdentical() {
        return currentVersion.equals(this.version);
    }

    /**
     * 获取过程是否异常
     *
     * @return 是否异常
     */
    public boolean isError() {
        return this.error;
    }
}