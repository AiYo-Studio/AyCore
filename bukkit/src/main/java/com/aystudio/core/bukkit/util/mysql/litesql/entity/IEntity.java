package com.aystudio.core.bukkit.util.mysql.litesql.entity;

import org.bukkit.configuration.file.FileConfiguration;

public interface IEntity {

    String getOwner();

    void setNewData(boolean newData);

    boolean isNewData();

    FileConfiguration toYaml();
}
