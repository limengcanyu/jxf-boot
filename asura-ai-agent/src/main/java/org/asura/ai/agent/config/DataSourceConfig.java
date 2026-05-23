package org.asura.ai.agent.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 数据源配置类
 * 配置 MyBatis Plus 的 Mapper 扫描路径
 */
@Configuration
@MapperScan("org.asura.ai.agent.mapper")
public class DataSourceConfig {
}