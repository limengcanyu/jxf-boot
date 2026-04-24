package com.spring.boot.multi.datasource.transaction.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus Config
 * <p>
 * 注意:
 * <p>
 * 如果内部插件都是使用,需要注意顺序关系,建议使用如下顺序
 * <p>
 * 多租户插件,动态表名插件
 * 分页插件,乐观锁插件
 * sql性能规范插件,防止全表更新与删除插件
 * 总结: 对sql进行单次改造的优先放入,不对sql进行改造的最后放入
 * <p>
 * 主体插件: MybatisPlusInterceptor
 * 该插件内部插件集:
 * <p>
 * 分页插件: PaginationInnerInterceptor
 * 多租户插件: TenantLineInnerInterceptor
 * 动态表名插件: DynamicTableNameInnerInterceptor
 * 乐观锁插件: OptimisticLockerInnerInterceptor
 * sql性能规范插件: IllegalSQLInnerInterceptor
 * 防止全表更新与删除插件: BlockAttackInnerInterceptor
 *
 * @author rock
 * @date 2019/11/19
 */
@MapperScan("com.spring.boot.multi.datasource.transaction.dao.mapper")
@Configuration
public class MybatisPlusConfig {

    @Bean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor() {
        TenantLineInnerInterceptor tenantLineInnerInterceptor = new TenantLineInnerInterceptor();
        TenantLineHandler tenantLineHandler = new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                return new StringValue("tenant_000001");
            }

            @Override
            public String getTenantIdColumn() {
                return "tenant_id";
            }

            @Override
            public boolean ignoreTable(String tableName) {
                return false;
            }
        };
        tenantLineInnerInterceptor.setTenantLineHandler(tenantLineHandler);

        return tenantLineInnerInterceptor;
    }

    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        return paginationInnerInterceptor;
    }

    @Bean
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor = new OptimisticLockerInnerInterceptor();
        return optimisticLockerInnerInterceptor;
    }

    @Bean
    public IllegalSQLInnerInterceptor illegalSQLInnerInterceptor() {
        IllegalSQLInnerInterceptor illegalSQLInnerInterceptor = new IllegalSQLInnerInterceptor();
        return illegalSQLInnerInterceptor;
    }

//    @Profile({"dev"})
    @Bean
    public BlockAttackInnerInterceptor blockAttackInnerInterceptor() {
        BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
        return blockAttackInnerInterceptor;
    }

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        interceptor.addInnerInterceptor(tenantLineInnerInterceptor());
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor());
//        interceptor.addInnerInterceptor(illegalSQLInnerInterceptor());
//        interceptor.addInnerInterceptor(blockAttackInnerInterceptor());
        return interceptor;
    }

}
