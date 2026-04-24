package com.spring.boot.mybatis.plus.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.spring.boot.mybatis.plus.service.DataChangeLogService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@MapperScan("com.spring.boot.mybatis.plus.mapper")
@Configuration
public class MybatisPlusConfig {

    @Autowired
    private DataChangeLogService dataChangeLogService;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
//            @Override
//            public Expression getTenantId() {
//                return new LongValue(1);
//            }
//
//            // 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
//            @Override
//            public boolean ignoreTable(String tableName) {
//                return !"sys_user".equalsIgnoreCase(tableName);
//            }
//        })); // 多租户插件 必须置于分页插件之前
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); // 分页插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor()); // 乐观锁插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor()); // 防全表更新与删除插件
        interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor()); // sql 性能规范插件
//        interceptor.addInnerInterceptor(new DataChangeRecorderInnerInterceptor(dataChangeLogService)); // 数据变动记录插件
        return interceptor;
    }

}
