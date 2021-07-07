package com.mc9y.blank038api.util.mysql.execute;

import java.sql.PreparedStatement;

/**
 * @author Blank038
 */
@FunctionalInterface
public interface ExecuteModel {

    /**
     * 连接数据库执行
     *
     * @param statement 命令提交
     */
    void run(PreparedStatement statement);
}