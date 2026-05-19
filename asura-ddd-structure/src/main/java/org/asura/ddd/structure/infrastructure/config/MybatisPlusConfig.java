package org.asura.ddd.structure.infrastructure.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.asura.ddd.structure.infrastructure.context.TenantContextHolder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({
    "org.asura.ddd.structure.user.infrastructure.persistence.mapper",
    "org.asura.ddd.structure.order.infrastructure.persistence.mapper",
    "org.asura.ddd.structure.inventory.infrastructure.persistence.mapper"
})
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 添加租户插件
        TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
        tenantInterceptor.setTenantLineHandler(customTenantHandler());
        interceptor.addInnerInterceptor(tenantInterceptor);

        // 添加分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));

        // 添加乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        return interceptor;
    }

    @Bean
    public TenantLineHandler customTenantHandler() {
        return new TenantLineHandler() {

            @Override
            public Expression getTenantId() {
                Long tenantId = TenantContextHolder.getCurrentTenantId();
                // 处理租户ID为空的情况（默认使用0作为公共租户）
                if (tenantId == null) {
                    tenantId = 0L;
                }
                return new LongValue(tenantId);
            }

            @Override
            public String getTenantIdColumn() {
                return "tenant_id";
            }

            @Override
            public boolean ignoreTable(String tableName) {
                // 可以配置忽略的表（如系统配置表）
                return false;
            }
        };
    }

}