package com.spring.boot.mybatis.plus.basic.init;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class H2Initializer {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public H2Initializer(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void checkDatabaseInitialization() throws SQLException {
        System.out.println("开始检查数据库初始化状态...");

        // 检查连接
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("数据库连接成功: " + connection.getMetaData().getURL());

            // 检查表是否存在
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "user", new String[]{"TABLE"});

            if (tables.next()) {
                System.out.println("user表创建成功!");

                // 查看表结构
                ResultSet columns = metaData.getColumns(null, null, "user", null);
                System.out.println("user表结构:");
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String typeName = columns.getString("TYPE_NAME");
                    System.out.println(columnName + " - " + typeName);
                }

                // 查看数据量
                Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user", Integer.class);
                System.out.println("user表数据量: " + count);
            } else {
                System.err.println("警告: user表未创建!");
            }
        } catch (SQLException e) {
            System.err.println("数据库检查失败: " + e.getMessage());
            throw e;
        }
    }
}

