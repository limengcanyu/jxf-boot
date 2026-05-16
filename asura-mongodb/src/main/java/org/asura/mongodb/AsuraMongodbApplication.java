package org.asura.mongodb;

import org.asura.mongodb.repository.CommonMongoRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableMongoRepositories(repositoryBaseClass = CommonMongoRepositoryImpl.class, basePackages = "org.asura.mongodb.repository")
@SpringBootApplication
public class AsuraMongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsuraMongodbApplication.class, args);
    }

}
