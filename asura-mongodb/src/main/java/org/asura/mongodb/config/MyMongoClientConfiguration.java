package org.asura.mongodb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MyMongoClientConfiguration extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "";
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }

}