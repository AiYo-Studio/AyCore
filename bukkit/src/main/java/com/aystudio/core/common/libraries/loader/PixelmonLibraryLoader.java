package com.aystudio.core.common.libraries.loader;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.util.file.LibFileDownload;
import com.aystudio.core.common.data.CommonData;

import java.io.File;

/**
 * @author Blank038
 */
@Deprecated
public class PixelmonLibraryLoader {
    private String fileName;
    private String url;

    public PixelmonLibraryLoader() {
        switch (CommonData.coreVersion) {
            case "v1_16_R3":
                this.url = "https://repo.mc9y.com/snapshots/com/aystudio/core/AyCore-Pixelmon/1.16.5-R0.1/AyCore-Pixelmon-1.16.5-R0.1.jar";
                this.fileName = "AyCore-Pixelmon-1.16.5-R0.1.jar";
                break;
            case "v1_12_R1":
                this.url = "https://repo.mc9y.com/snapshots/com/aystudio/core/AyCore-Pixelmon/1.12.2-R0.1/AyCore-Pixelmon-1.12.2-R0.1.jar";
                this.fileName = "AyCore-Pixelmon-1.12.2-R0.1.jar";
                break;
            default:
                break;
        }
    }

    public void pull() {
        if (this.fileName == null || this.url == null) {
            return;
        }
        File folder = new File(AyCore.getInstance().getDataFolder(), "pixelmon");
        folder.mkdir();
        File file = new File(folder, this.fileName);
        new LibFileDownload(this.url, file).start();
    }
}