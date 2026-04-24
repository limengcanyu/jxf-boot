package com.spring.boot.questdb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;
import java.util.Properties;

@SpringBootTest
class SpringBootQuestdbApplicationTests {

    @Test
    void insert() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "admin");
        properties.setProperty("password", "quest");
        properties.setProperty("sslmode", "disable");

        final Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:8812/qdb", properties);
        connection.setAutoCommit(false);

        final PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS trades (ts TIMESTAMP, date DATE, name STRING, value INT) timestamp(ts);");
        statement.execute();

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TRADES  VALUES (?, ?, ?, ?)")) {
            preparedStatement.setTimestamp(1, new Timestamp(io.questdb.std.Os.currentTimeMicros()));
            preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
            preparedStatement.setString(3, "abc");
            preparedStatement.setInt(4, 123);
            preparedStatement.execute();
        }

        System.out.println("Done");
        connection.close();
    }

    @Test
    void query() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "admin");
        properties.setProperty("password", "quest");
        properties.setProperty("sslmode", "disable");

        final Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:8812/qdb", properties);

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT x FROM long_sequence(5);")) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getLong(1));
                }
            }
        }

        connection.close();
    }

}
