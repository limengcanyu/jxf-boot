package com.spring.boot.mybatis.plus.basic;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.ibatis.annotations.Mapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodeGenerator {
    // 数据库配置
    private static final String URL = "jdbc:h2:file:./data/testdb;DB_CLOSE_DELAY=-1;MODE=MYSQL;DATABASE_TO_UPPER=false";
//    private static final String URL = "jdbc:mysql://localhost:3306/rock-boot?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "test";
    private static final String[] TABLES = {"user", "role", "user_role", "product", "order"};

    // 生成配置
    private static final String PROJECT_PATH = System.getProperty("user.dir");
    private static final String MODULE_NAME = "spring-boot-mybatis-plus-basic";
    private static final String OUTPUT_DIR = PROJECT_PATH + "/" + MODULE_NAME + "/src/main/java";
    private static final String MAPPER_XML_OUTPUT_DIR = PROJECT_PATH + "/" + MODULE_NAME + "/src/main/resources/mapper";
    private static final String PARENT_PACKAGE = "com.spring.boot.mybatis.plus.basic";

    public static void main(String[] args) throws SQLException {
        checkDatabaseConnection();

        checkTablesExist();

        generateCode();

        System.out.println("代码生成完成");
    }

    /**
     * 检查数据库连接是否正常
     */
    private static void checkDatabaseConnection() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            System.out.println("数据库连接成功：" + URL);
        } catch (SQLException e) {
            throw new SQLException("数据库连接失败：" + e.getMessage(), e);
        }
    }

    /**
     * 检查要生成的表是否存在
     */
    private static void checkTablesExist() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            List<String> existingTables = new ArrayList<>();
            while (tables.next()) {
                existingTables.add(tables.getString("TABLE_NAME").toLowerCase());
            }

            // 检查每个表是否存在
            for (String table : TABLES) {
                if (!existingTables.contains(table.toLowerCase())) {
                    System.err.println("警告：表 '" + table + "' 不存在于数据库中");
                }
            }
        }
    }

    /**
     * 执行代码生成
     */
    private static void generateCode() {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                .globalConfig(builder -> builder
                        .author("jxf")
                        .enableSpringdoc()
                        .outputDir(OUTPUT_DIR)
                        .commentDate("yyyy-MM-dd")
                )
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.SMALLINT) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder -> builder
                                .parent(PARENT_PACKAGE)
                                .entity("entity")
                                .mapper("mapper")
                                .service("service")
                                .serviceImpl("service.impl")
                                .xml("mapper.xml")
//                        .moduleName("system") // 设置父包模块名
                                .pathInfo(Collections.singletonMap(OutputFile.xml, MAPPER_XML_OUTPUT_DIR)) // 设置mapperXml生成路径
                )
                .strategyConfig(builder -> builder
                        .addInclude("user") // 设置需要生成的表名
                        .addTablePrefix("t_", "c_") // 设置过滤表前缀
                        .entityBuilder()
                        .enableLombok()
                        .controllerBuilder()
                        .enableRestStyle()
                        .serviceBuilder()
                        .formatServiceFileName("%sService")
                        .formatServiceImplFileName("%sServiceImpl")
                        .mapperBuilder()
                        .formatMapperFileName("%sMapper")
                        .mapperAnnotation(Mapper.class)
                        .enableBaseResultMap()
                        .enableBaseColumnList()
                        .build()
                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
