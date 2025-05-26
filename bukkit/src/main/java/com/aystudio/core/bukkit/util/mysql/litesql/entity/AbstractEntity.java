package com.aystudio.core.bukkit.util.mysql.litesql.entity;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public abstract class AbstractEntity implements IEntity {
    private final String owner;
    private final FileConfiguration data;
    private boolean newData;

    public AbstractEntity(String owner, FileConfiguration data) {
        this.owner = owner;
        this.data = data;
        if (data.getBoolean("new")) {
            this.setNewData(true);
        }
    }

    @Override
    public void setNewData(boolean newData) {
        this.newData = newData;
    }
}
