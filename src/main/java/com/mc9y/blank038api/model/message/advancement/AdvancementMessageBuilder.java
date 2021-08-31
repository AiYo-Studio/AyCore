package com.mc9y.blank038api.model.message.advancement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mc9y.blank038api.enums.AdvancementBackgroundType;
import com.mc9y.blank038api.enums.FrameType;

import java.util.UUID;

/**
 * 进阶消息构造器
 *
 * @author Blank038
 * @since 2021-09-01
 */
public class AdvancementMessageBuilder {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private String icon = "minecraft:stone";
    private String message = "";
    private AdvancementBackgroundType backgroundType = AdvancementBackgroundType.ADVENTURE;
    private FrameType frameType = FrameType.GOAL;

    public AdvancementMessageBuilder background(AdvancementBackgroundType backgroundType) {
        this.backgroundType = backgroundType;
        return this;
    }

    public AdvancementMessageBuilder msg(String message) {
        this.message = message;
        return this;
    }

    public AdvancementMessageBuilder setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public AdvancementMessageBuilder setFrame(FrameType type) {
        this.frameType = type;
        return this;
    }

    public AdvancementMessage build() {
        JsonObject basicJson = new JsonObject();
        JsonObject display = new JsonObject();
        JsonObject icon = new JsonObject();
        icon.addProperty("item", this.icon);
        display.add("icon", icon);
        display.addProperty("title", this.message);
        display.addProperty("background", String.format("minecraft:textures/gui/advancements/backgrounds/%s.png", this.backgroundType.name().toLowerCase()));
        display.addProperty("frame", this.frameType.name().toLowerCase());
        display.addProperty("announce_to_chat", false);
        display.addProperty("show_toast", true);
        display.addProperty("description", "aaaaa");
        display.addProperty("hidden", true);
        JsonObject trigger = new JsonObject();
        trigger.addProperty("trigger", "minecraft:impossible");
        JsonObject criteria = new JsonObject();
        criteria.add(UUID.randomUUID().toString(), trigger);
        basicJson.add("criteria", criteria);
        basicJson.add("display", display);
        return new AdvancementMessage(GSON.toJson(basicJson));
    }
}
