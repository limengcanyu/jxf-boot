package com.spring.boot.mybatisplus.druid.multi.datasource.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: Mybatis-Plus配置类</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/24 0024
 */
@Configuration
@MapperScan({
        "com.spring.boot.mybatisplus.druid.multi.datasource.artanis.dao.mapper",
        "com.spring.boot.mybatisplus.druid.multi.datasource.samuro.dao.mapper",
        "com.spring.boot.mybatisplus.druid.multi.datasource.valeera.dao.mapper"
})
public class MybatisPlusConfig {

    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//        paginationInterceptor.setLocalPage(true); // 开启 PageHelper localPage 模式
        return paginationInterceptor;
    }

    /**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     */
    @Bean
    @Profile({"dev", "test"})// 设置 dev test 环境开启
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * mybatis-plus 乐观锁拦截器
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    @Bean(name = "dbArtanis")
    @ConfigurationProperties(prefix = "spring.datasource.druid.artanis")
    public DataSource dbArtanis() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dbSamuro")
    @ConfigurationProperties(prefix = "spring.datasource.druid.samuro")
    public DataSource dbSamuro() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dbValeera")
    @ConfigurationProperties(prefix = "spring.datasource.druid.valeera")
    public DataSource dbValeera() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 动态数据源配置
     *
     * @return
     */
    @Bean
    @Primary
    public DataSource multipleDataSource(@Qualifier("dbArtanis") DataSource dbArtanis,
                                         @Qualifier("dbSamuro") DataSource dbSamuro,
                                         @Qualifier("dbValeera") DataSource dbValeera) {
        //数据源Map
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.dbArtanis.getValue(), dbArtanis);
        targetDataSources.put(DBTypeEnum.dbSamuro.getValue(), dbSamuro);
        targetDataSources.put(DBTypeEnum.dbValeera.getValue(), dbValeera);

        MultiDataSource multiDataSource = new MultiDataSource();
        //目标数据源
        multiDataSource.setTargetDataSources(targetDataSources);
        //默认数据源
        multiDataSource.setDefaultTargetDataSource(dbArtanis);

        return multiDataSource;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(multipleDataSource(dbArtanis(), dbSamuro(), dbValeera()));
        //sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/*/*Mapper.xml"));

        MybatisConfiguration configuration = new MybatisConfiguration();
        //configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        sqlSessionFactory.setPlugins(new Interceptor[]{
                performanceInterceptor(), //Mybatis-Plus SQL执行效率插件拦截器
                // OptimisticLockerInterceptor(), //Mybatis-Plus 乐观锁拦截器
                paginationInterceptor() //Mybatis-Plus 分页拦截器
        });
//        sqlSessionFactory.setGlobalConfig(globalConfiguration()); //关闭Mybatis-Plus逻辑删除配置
        return sqlSessionFactory.getObject();
    }

    /**
     * Mybatis-Plus逻辑删除配置
     *
     * @return
     */
    @Bean
    public GlobalConfiguration globalConfiguration() {
        GlobalConfiguration conf = new GlobalConfiguration(new LogicSqlInjector());
        conf.setLogicDeleteValue("-1");
        conf.setLogicNotDeleteValue("1");
        conf.setIdType(0);
//        conf.setMetaObjectHandler(new MyMetaObjectHandler());
        conf.setDbColumnUnderline(true);
        conf.setRefresh(true);
        return conf;
    }
}
