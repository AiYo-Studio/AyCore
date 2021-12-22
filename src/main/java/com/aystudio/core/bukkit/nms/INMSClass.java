package com.aystudio.core.bukkit.nms;


import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.nms.packet.EnumPacket;
import com.aystudio.core.bukkit.util.key.KeyListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 原版方法类, 用于 NMS 类实现接口
 *
 * @author Blank038
 */
public abstract class INMSClass {
    private Class<?> sendPacketParamsClass;

    public INMSClass() {
        try {
            this.sendPacketParamsClass = Class.forName("net.minecraft.server." + this.getVID() + ".Packet");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void registerChannel(AyCore instance) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(instance, "keyexecute");
        Bukkit.getMessenger().registerIncomingPluginChannel(instance, "keyexecute", new KeyListener());
    }


    /**
     * 向玩家发送无闪 Title
     *
     * @param player   目标玩家
     * @param title    主标题
     * @param subTitle 副标题
     * @param in       渐入时间
     * @param stay     持续时间
     * @param out      淡化时间
     */
    public void sendTitle(Player player, String title, String subTitle, int in, int stay, int out) {
        if (player != null && player.isOnline()) {
            try {
                Class<?> craftPlayer = player.getClass();
                Method handlerMethod = craftPlayer.getMethod("getHandle");
                Object entityPlayer = handlerMethod.invoke(player);
                Field field = entityPlayer.getClass().getField("playerConnection");
                Object playerConnection = field.get(entityPlayer);
                // 初始化包
                Class<?> enumTitleAction = this.classForName("net.minecraft.server.?.PacketPlayOutTitle$EnumTitleAction");
                Object titleActionMain = this.getField(enumTitleAction, "TITLE", false),
                        titleActionSub = this.getField(enumTitleAction, "SUBTITLE", false);
                this.sendPacket(playerConnection, this.createPacket(EnumPacket.PacketPlayOutTitle_1, titleActionMain, this.createChatComponentText(title)));
                this.sendPacket(playerConnection, this.createPacket(EnumPacket.PacketPlayOutTitle_1, titleActionSub, this.createChatComponentText(subTitle)));
                this.sendPacket(playerConnection, this.createPacket(EnumPacket.PacketPlayOutTitle_2, in, stay, out));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendPacket(Object playerConnection, Object packet) {
        try {
            Method method = playerConnection.getClass().getMethod("sendPacket", this.sendPacketParamsClass);
            method.invoke(playerConnection, packet);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object createChatComponentText(String text) {
        try {
            Class<?> c = Class.forName("net.minecraft.server." + this.getVID() + ".ChatComponentText");
            Constructor<?> constructor = c.getConstructor(String.class);
            return constructor.newInstance(ChatColor.translateAlternateColorCodes('&', text));
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class<?> classForName(String path) {
        try {
            return Class.forName(path.replace("?", this.getVID()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object createPacket(EnumPacket packet, Object... params) {
        try {
            Class<?> c = Class.forName(packet.getClassPath().replace("?", this.getVID()));
            Object[] tarParams = packet.getParams();
            Class<?>[] classes = new Class[tarParams.length];
            for (int i = 0; i < tarParams.length; i++) {
                if (tarParams[i] instanceof String) {
                    classes[i] = Class.forName(((String) tarParams[i]).replace("?", this.getVID()));
                } else {
                    classes[i] = (Class<?>) tarParams[i];
                }
            }
            Constructor<?> constructor = c.getConstructor(classes);
            return constructor.newInstance(params);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object invoke(Object obj, String methodName, Class<?>[] paramsClass, Object... params) {
        try {
            Class<?> c = obj.getClass();
            Method method = c.getMethod(methodName, paramsClass);
            method.invoke(obj, params);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getField(Object obj, String fieldName, boolean getClass) {
        try {
            Field[] field = getClass ? obj.getClass().getFields() : ((Class<?>) obj).getFields();
            for (Field i : field) {
                if (i.getName().equals(fieldName)) {
                    return i.get(obj);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取 nms 版本包符
     *
     * @return 版本包符
     */
    public abstract String getVID();
}
