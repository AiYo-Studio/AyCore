package com.aystudio.core.forge;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.util.custom.LoggerUtil;
import com.aystudio.core.common.data.CommonData;
import com.aystudio.core.common.link.ILink;
import com.aystudio.core.forge.enums.CoreTypeEnum;
import com.aystudio.core.forge.hook.bukkit.nms.impl.Forge112Impl;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * @author Blank038
 * @since 2022-01-09
 */
public class ForgeInject implements ILink {
    @Getter
    private static ForgeInject instance;
    @Getter
    @Setter
    private IForgeListenHandler forgeListener;

    public ForgeInject() {
        instance = this;
        switch (CommonData.coreVersion) {
            case "v1_12_R1":
                AyCore.getInstance().setNMSClass(new Forge112Impl());
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoad() {
        // 注册 Forge 事件监听
        boolean forward = AyCore.getInstance().getConfig().getBoolean("forward_forge_event", true);
        if (forward) {
            Arrays.stream(CoreTypeEnum.values()).forEach((e) -> e.checkAndInit().registerListener());
        } else {
            LoggerUtil.getOrRegister(AyCore.class).log("&f无挂钩核心载入");
        }
    }
}
