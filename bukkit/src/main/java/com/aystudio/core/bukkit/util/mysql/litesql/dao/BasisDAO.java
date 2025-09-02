package com.aystudio.core.bukkit.util.mysql.litesql.dao;

import com.aystudio.core.bukkit.util.mysql.MySqlStorageHandler;
import com.aystudio.core.bukkit.util.mysql.litesql.entity.IEntity;
import lombok.Setter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Level;

public class BasisDAO<T extends IEntity> {
    private final JavaPlugin source;
    private final Class<? extends IEntity> entityClass;
    private final MySqlStorageHandler storageHandler;
    private final String table;
    @Setter
    private Function<String, T> getterFunc;

    public BasisDAO(JavaPlugin plugin, Class<T> entityClass, String url, String user, String password, String table) {
        this.source = plugin;
        this.entityClass = entityClass;
        this.table = table;
        String[] sqlArray = {
                "CREATE TABLE IF NOT EXISTS " + table + " (user VARCHAR(30) NOT NULL, data TEXT, locked INT, PRIMARY KEY ( user ))"
        };
        this.storageHandler = new MySqlStorageHandler(plugin, url, user, password, sqlArray);
        this.storageHandler.setCheckConnection(true);
        this.storageHandler.setReconnectionQueryTable(table);
    }

    public void setLocked(Player player, boolean locked) {
        this.storageHandler.connect((statement) -> {
            try {
                statement.setInt(1, (locked ? 0 : 1));
                statement.setString(2, player.getName());
                statement.executeUpdate();
            } catch (SQLException e) {
                source.getLogger().log(Level.WARNING, e, () -> "Failed to set locked");
            }
        }, "UPDATE " + table + " SET locked=? WHERE user=?");
    }

    public T get(String name) {
        T t;
        if (getterFunc != null && (t = getterFunc.apply(name)) != null) {
            return t;
        }
        AtomicReference<FileConfiguration> atomicReference = new AtomicReference<>();
        this.storageHandler.connect((statement) -> {
            try {
                statement.setString(1, name);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String data = resultSet.getString("data");
                    if (data != null) {
                        FileConfiguration object = new YamlConfiguration();
                        object.loadFromString(new String(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8))));
                        atomicReference.set(object);
                    }
                }
                resultSet.close();
            } catch (SQLException | InvalidConfigurationException e) {
                source.getLogger().log(Level.WARNING, e, () -> "Failed to load entity data");
            }
        }, "SELECT data FROM " + table + " WHERE user=?");
        if (atomicReference.get() == null) {
            FileConfiguration object = new YamlConfiguration();
            object.set("new", true);
            atomicReference.set(object);
        }
        try {
            return (T) entityClass.getConstructor(String.class, FileConfiguration.class).newInstance(name, atomicReference.get());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            this.source.getLogger().log(Level.WARNING, e, () -> "Failed to create entity data");
            return null;
        }
    }

    public boolean isLocked(Player player) {
        AtomicReference<Boolean> result = new AtomicReference<>();
        result.set(false);
        this.storageHandler.connect((statement) -> {
            try {
                statement.setString(1, player.getName());
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int locked = resultSet.getInt("locked");
                    if (locked == 0) {
                        result.set(true);
                        break;
                    }
                }
                resultSet.close();
            } catch (SQLException e) {
                source.getLogger().log(Level.WARNING, e, () -> "Failed to call isLocked()");
            }
        }, "SELECT locked FROM " + table + " WHERE user=?");
        return result.get();
    }

    public void save(IEntity data, int locked) {
        FileConfiguration object = data.toYaml();
        String text = new String(Base64.getEncoder().encode(object.saveToString().getBytes(StandardCharsets.UTF_8)));
        String sql = String.format(data.isNewData() ? "INSERT INTO " + table + " (user,data,locked) VALUES (?,?,%s)" : "UPDATE " + table + " SET data=?, locked='%s' WHERE user=?", locked);
        this.storageHandler.connect((statement) -> {
            try {
                if (data.isNewData()) {
                    statement.setString(1, data.getOwner());
                    statement.setString(2, text);
                    data.setNewData(false);
                } else {
                    statement.setString(1, text);
                    statement.setString(2, data.getOwner());
                }
                statement.executeUpdate();
            } catch (SQLException e) {
                source.getLogger().log(Level.WARNING, e, () -> "Failed to save entity data");
            }
        }, sql);
    }
}
