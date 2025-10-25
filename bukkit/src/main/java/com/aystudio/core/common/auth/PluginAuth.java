package com.aystudio.core.common.auth;

import com.aystudio.core.bukkit.AyCore;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PluginAuth {

    private static final String LOCALHOST_IP = "127.0.0.1";
    private static final int SOCKET_TIMEOUT = 5000;
    private static final String AUTH_JSON_FORMAT = "{\n" +
            "    \"Plugin\": \"%s\",\n" +
            "    \"Version\": \"%s\",\n" +
            "    \"Key\": \"%s\"\n" +
            "}";

    private final JavaPlugin plugin;
    private final String key;
    private final String serverIP;
    private final int port;
    private final String classPath;

    public PluginAuth(JavaPlugin plugin, String key, String serverIP, int port, String classPath) {
        this.plugin = plugin;
        this.key = key;
        this.serverIP = serverIP;
        this.port = port;
        this.classPath = classPath;

        performAuthentication();
    }

    private void performAuthentication() {
        Socket socket = null;

        try {
            socket = createSocketConnection();

            if (shouldSkipAuthentication(socket)) {
                return;
            }

            byte[] authData = receiveAuthenticationData(socket);
            loadAuthenticatedClass(authData);

        } catch (UnknownHostException e) {
            logAuthenticationError("无法解析主机地址: " + serverIP, e);
        } catch (SocketTimeoutException e) {
            logAuthenticationError("连接超时", e);
        } catch (IOException e) {
            logAuthenticationError("网络通信错误", e);
        } catch (ReflectiveOperationException e) {
            logAuthenticationError("类加载失败", e);
        } catch (SecurityException e) {
            logAuthenticationError("安全验证失败", e);
        } finally {
            closeSocketSafely(socket);
        }
    }

    private Socket createSocketConnection() throws IOException {
        Socket socket = new Socket(serverIP, port);
        socket.setSoTimeout(SOCKET_TIMEOUT);
        return socket;
    }

    private boolean shouldSkipAuthentication(Socket socket) {
        String remoteAddress = socket.getInetAddress().getHostAddress();
        return LOCALHOST_IP.equals(remoteAddress);
    }

    private byte[] receiveAuthenticationData(Socket socket) throws IOException {
        try (OutputStream os = socket.getOutputStream();
             InputStream is = socket.getInputStream();
             ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {

            String authJson = String.format(AUTH_JSON_FORMAT, plugin.getName(), plugin.getDescription().getVersion(), key);
            byte[] encodedData = Base64.getEncoder().encode(authJson.getBytes(StandardCharsets.UTF_8));
            os.write(encodedData);
            os.flush();
            socket.shutdownOutput();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                byteArray.write(buffer, 0, bytesRead);
            }

            socket.shutdownInput();
            return byteArray.toByteArray();
        }
    }

    private void loadAuthenticatedClass(byte[] classData) throws ReflectiveOperationException {
        validateClassPath(classPath);

        ClassLoader classLoader = AyCore.class.getClassLoader();

        try {
            Method defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass",
                    String.class, byte[].class, int.class, int.class);
            defineClassMethod.setAccessible(true);

            Class<?> authenticatedClass = (Class<?>) defineClassMethod.invoke(
                    classLoader, classPath, classData, 0, classData.length);

            Constructor<?> constructor = authenticatedClass.getConstructor();
            Object instance = constructor.newInstance();

            Method onLoadMethod = authenticatedClass.getMethod("onLoad");
            onLoadMethod.invoke(instance);

        } catch (InvocationTargetException e) {
            throw new ReflectiveOperationException(String.format("认证类执行失败(%s bytes)", classData.length), e.getTargetException());
        }
    }

    private void validateClassPath(String path) throws SecurityException {
        if (path == null || path.trim().isEmpty()) {
            throw new SecurityException("类路径不能为空");
        }

        if (path.contains("..") || path.contains("/") || path.contains("\\")) {
            throw new SecurityException("不安全的类路径: " + path);
        }
    }

    private void closeSocketSafely(Socket socket) {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                if (isDebugMode()) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void logAuthenticationError(String message, Exception ex) {
        String errorMessage = String.format("&f[%s&f] &cAuthentication failed, reason: &7%s - %s",
                plugin.getName(), message, ex.getLocalizedMessage());
        AyCore.getInstance().getConsoleLogger().log(false, errorMessage);

        if (isDebugMode()) {
            ex.printStackTrace();
        }
    }

    private boolean isDebugMode() {
        return AyCore.getInstance().getConfig().getBoolean("debug", false);
    }
}
