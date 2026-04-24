package com.spring.boot.data.elasticsearch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/05/17 23:13
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.spring.boot.data.elasticsearch.repository")
public class ElasticsearchConfig {
}
