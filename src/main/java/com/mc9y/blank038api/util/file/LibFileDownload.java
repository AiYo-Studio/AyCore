package com.mc9y.blank038api.util.file;

import com.mc9y.blank038api.Blank038API;
import com.mc9y.pokemonapi.PokemonAPI;

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

    public void load() {
        Method method = null;
        try {
            method = Blank038API.class.getClassLoader().getClass().getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(Blank038API.class.getClassLoader(), file.toURI().toURL());
        } catch (MalformedURLException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
        } finally {
            if (method != null) {
                method.setAccessible(false);
            }
        }
    }
}