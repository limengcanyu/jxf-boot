package com.spring.boot.multi.datasource.transaction.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * description:
 *
 * @author rock
 * time 2020/7/7 0007 14:14
 */
@EnableTransactionManagement
@Configuration
public class DataSourceConfig {
    /**
     * 默认事务管理器，即默认MySQL事务
     *
     * @param dataSource
     * @return
     */
    @Primary
    @Bean("dataSourceTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager(HikariDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * mongo操作需要事务支持时需指定事务管理器
     * <p>
     * 例如，@Transactional(transactionManager="mongoTransactionManager")
     *
     * @param mongoDatabaseFactory
     * @return
     */
    @Bean(name = "mongoTransactionManager")
    public MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTransactionManager(mongoDatabaseFactory);
    }

}
