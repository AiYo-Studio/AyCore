package com.aystudio.core.bukkit.util.custom;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.common.data.CommonData;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;

/**
 * 目标插件版本检测
 *
 * @author Blank038
 * @since 2021-05-13
 */
public class VerCheck {
    private final String currentVersion;
    private String version;
    @Getter
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
        } catch (Exception e) {
            if (CommonData.debug) {
                AyCore.getInstance().getLogger().log(Level.SEVERE, e, () -> "版本检测异常");
            }
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

}