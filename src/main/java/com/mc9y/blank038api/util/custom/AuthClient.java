package com.mc9y.blank038api.util.custom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mc9y.blank038api.interfaces.CustomExecute;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.*;
import java.util.HashMap;

/**
 * @author Blank038
 * @since 2021-08-23
 */
public class AuthClient {
    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().create();

    private final JavaPlugin SOURCE_PLUGIN;
    private final String SERVER_IP, AUTH_KEY, CHECK_VER_URL;
    private final CustomExecute<Boolean> CUSTOM_EXECUTE;
    private final int SERVER_PORT;
    private boolean auth;
    private int reconnCount = 1;

    public AuthClient(JavaPlugin plugin, String serverIp, int port, String key, String checkVerUrl, CustomExecute<Boolean> execute) {
        this.SOURCE_PLUGIN = plugin;
        this.SERVER_IP = serverIp;
        this.CHECK_VER_URL = checkVerUrl;
        this.AUTH_KEY = key;
        this.SERVER_PORT = port;
        this.CUSTOM_EXECUTE = execute;
    }

    public void sendAuthPacket() {
        Bukkit.getConsoleSender().sendMessage("§f================ §a" + this.SOURCE_PLUGIN.getDescription().getName() + " §f================");
        Bukkit.getConsoleSender().sendMessage("§f > §e正在连接校验服务器...");
        while (reconnCount < 4) {
            if (this.connect()) {
                this.auth = true;
                break;
            }
            reconnCount++;
            // 沉睡 0.5s
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Bukkit.getConsoleSender().sendMessage("§f=============================================");
        if (!auth) {
            Bukkit.getPluginManager().disablePlugin(this.SOURCE_PLUGIN);
        }
    }

    public boolean connect() {
        try {
            Socket socket = new Socket(this.SERVER_IP, this.SERVER_PORT);
            socket.setSoTimeout(5000);
            String mac;
            String osName = System.getProperty("os.name");
            if (osName.startsWith("Windows")) {
                mac = this.getMAC();
            } else {
                mac = "Linux";
            }
            if (socket.isConnected()) {
                if (mac == null || socket.getInetAddress().getHostAddress().equals(new String(new byte[]{49, 50, 55, 46, 48, 46, 48, 46, 49}))) {
                    socket.close();
                    Bukkit.getConsoleSender().sendMessage(" §f> §c获取主机地址出错, 尝试第 §f" + this.reconnCount + "§c 次重连.");
                    return false;
                }
                Bukkit.getConsoleSender().sendMessage("§f > §e校验服务器连接成功...");
                HashMap<String, String> jsonObject = new HashMap<>(3);
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                jsonObject.put("Plugin", this.SOURCE_PLUGIN.getDescription().getName());
                jsonObject.put("MAC", mac);
                jsonObject.put("Key", this.AUTH_KEY);
                bw.write(GSON.toJson(jsonObject));
                bw.flush();
                socket.shutdownOutput();

                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                Gson gson1 = new Gson();
                JsonObject jsonObject1 = gson1.fromJson(sb.toString(), JsonObject.class);
                if (jsonObject1 != null && jsonObject1.has("Status")) {
                    switch (StatusEnum.valueOf(jsonObject1.get("Status").getAsString())) {
                        case FailKey:
                            Bukkit.getConsoleSender().sendMessage("§f > §c卡密不存在, 激活失败");
                            break;
                        case FailPlugin:
                            Bukkit.getConsoleSender().sendMessage("§f > §c该插件不在校验列表中, 激活失败");
                            break;
                        case Allow:
                            try {
                                // 执行的内容
                                this.CUSTOM_EXECUTE.run(true);
                                // 公告及版本检测
                                Bukkit.getConsoleSender().sendMessage("§f > §a校验成功, 插件启动, 感谢支持正版");
                                // 检查插件版本
                                if (this.CHECK_VER_URL != null) {
                                    VerCheck check = new VerCheck(this.SOURCE_PLUGIN, this.CHECK_VER_URL);
                                    if (check.isError()) {
                                        Bukkit.getConsoleSender().sendMessage("§f > §c检测新版本异常, 不影响使用");
                                    } else {
                                        if (check.isIdentical()) {
                                            Bukkit.getConsoleSender().sendMessage("§f > §a插件已是最新版, 无需更新");
                                        } else {
                                            Bukkit.getConsoleSender().sendMessage("§f > §b插件可更新, 最新版: §f" + check.getVersion());
                                        }
                                    }
                                }
                                // 获取广告列表
                                ADClient adClient = new ADClient("http://www.mc9y.com/auth/ad/");
                                Bukkit.getConsoleSender().sendMessage("§f > §e广告列表:");
                                for (String ad : adClient.getADList()) {
                                    Bukkit.getConsoleSender().sendMessage("§f  - " + ad);
                                }
                            } catch (Exception e) {
                                break;
                            }
                            break;
                        default:
                            Bukkit.getConsoleSender().sendMessage("§f > §c校验失败, 请检查自己的配置");
                            break;
                    }
                } else {
                    Bukkit.getConsoleSender().sendMessage("§f > §c校验失败, 请检查自己的配置");
                }
                socket.shutdownInput();
                br.close();
                isr.close();
                is.close();
                br.close();
                bw.close();
                osw.close();
                os.close();
                socket.close();
                return true;
            } else {
                socket.close();
                Bukkit.getConsoleSender().sendMessage(" §f> §c连接服务器失败, 尝试第 §f" + this.reconnCount + "§c 次重连.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(" §f> §c出现异常错误, 尝试第 §f" + this.reconnCount + "§c 次重连.");
            return false;
        }
    }

    private String getMAC() {
        byte[] mac;
        try {
            mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        } catch (SocketException | UnknownHostException e) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : mac) {
            int temp = b & 0xff;
            String str = Integer.toHexString(temp);
            if (str.length() == 1) {
                sb.append("0").append(str);
            } else {
                sb.append(str);
            }
        }
        return sb.toString().toUpperCase();
    }

    private enum StatusEnum {
        FailKey,
        FailPlugin,
        Allow
    }
}
