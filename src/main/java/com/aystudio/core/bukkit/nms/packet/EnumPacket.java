package com.aystudio.core.bukkit.nms.packet;

/**
 * @author Blank038
 * @since 2021-07-07
 */
public enum EnumPacket {
    PacketPlayOutTitle_1("net.minecraft.server.?.PacketPlayOutTitle", new Object[]{
            "net.minecraft.server.?.PacketPlayOutTitle$EnumTitleAction",
            "net.minecraft.server.?.IChatBaseComponent"
    }),
    PacketPlayOutTitle_2("net.minecraft.server.?.PacketPlayOutTitle", new Object[]{
            int.class,
            int.class,
            int.class
    });;

    private final String CLASS_PATH;
    private final Object[] PARAMS;

    EnumPacket(String c, Object[] params) {
        this.CLASS_PATH = c;
        this.PARAMS = params;
    }

    public String getClassPath() {
        return this.CLASS_PATH;
    }

    public Object[] getParams() {
        return this.PARAMS;
    }
}
