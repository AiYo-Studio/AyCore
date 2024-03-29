package com.aystudio.core.bukkit.util.file;

import com.aystudio.core.bukkit.AyCore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;

/**
 * @author Blank038
 */
public class LibFileDownload {
    private final String url;
    private final File file;

    public LibFileDownload(String url, File file) {
        this.url = url;
        this.file = file;
    }

    public void start() {
        try {
            URL url = new URL(this.url);
            URLConnection uc = url.openConnection();
            InputStream is = uc.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);
            int length;
            byte[] bytes = new byte[1024];
            while ((length = is.read(bytes)) != -1) {
                fos.write(bytes, 0, length);
            }
            fos.flush();
            is.close();
            fos.close();
            success();
            load();
        } catch (Exception e) {
            e.printStackTrace();
            deny();
        }
    }

    public void success() {
    }

    public void deny() {
    }

    public boolean load() {
        Method method = null;
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(AyCore.class.getClassLoader(), file.toURI().toURL());
        } catch (MalformedURLException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (method != null) {
                method.setAccessible(false);
            }
        }
        return true;
    }
}