package com.spring.boot.data.r2dbc;

import com.github.jasync.r2dbc.mysql.JasyncConnectionFactory;
import com.github.jasync.sql.db.Configuration;
import com.github.jasync.sql.db.mysql.pool.MySQLConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

@Slf4j
class MySQLTests {

    @Test
    void mysql() {
        MySQLConnectionFactory mycf = new MySQLConnectionFactory(
                new Configuration("root", "192.168.242.129", 3306, "PassW0rd321", "artanis"));

        JasyncConnectionFactory cf = new JasyncConnectionFactory(mycf);

        DatabaseClient client = DatabaseClient.create(cf);

        client.sql("SELECT * FROM sys_user")
                .map((row, rowMetadata) -> row.get("name"))
                .all()
                .doOnNext(it -> {
                    System.out.println("Record: " + it);
                })
                .blockFirst();
//                .as(StepVerifier::create)
//                .expectNextCount(585)
//                .verifyComplete();

        // Unknown authentication method -> 'caching_sha2_password'
    }

}
