package com.spring.boot.data.mongodb;

import com.spring.boot.data.mongodb.repository.CommonMongoRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(repositoryBaseClass = CommonMongoRepositoryImpl.class, basePackages = "com.spring.boot.data.mongodb.repository")
@SpringBootApplication
public class SpringBootDataMongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDataMongodbApplication.class, args);
    }

}
