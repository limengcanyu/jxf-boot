package com.spring.boot.mybatis.plus.config;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.spring.boot.mybatis.plus.entity.DataChangeLog;
import com.spring.boot.mybatis.plus.service.DataChangeLogService;
import com.spring.boot.mybatis.plus.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;

@Slf4j
//@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class DataChangeInterceptor implements Interceptor {

    /**
     * 注入日志变更服务
     */
    @Autowired
    private DataChangeLogService dataChangeLogService;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.info("MybatisInterceptor intercept ===========================");
        log.info("MybatisInterceptor intercept Current thread id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());

        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = args[1];

        BoundSql boundSql = mappedStatement.getBoundSql(parameter);

        // data_change_log表不处理
        if (boundSql.getSql().contains("data_change_log")) {
            log.info("data_change_log表不处理");
            return invocation.proceed();
        }

        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        log.info("SqlCommandType: {}", sqlCommandType);

        // 获取sql
        String sql = showSql(mappedStatement.getConfiguration(), boundSql);
        log.info("sql: {}", sql);

        saveSql(sql);

        return invocation.proceed();
    }

    public void saveSql(String sql) {
        DataChangeLog dataChangeLog = new DataChangeLog();
        dataChangeLog.setOperationResult(sql);
        Map<String, Object> currentUser = UserContext.get();
        dataChangeLog.setOperatorId(currentUser == null ? null : currentUser.get("userId").toString());
        dataChangeLog.setOperatorName(currentUser == null ? null : currentUser.get("username").toString());
        dataChangeLog.setCreateTime(LocalDateTime.now());
        dataChangeLogService.save(dataChangeLog);
    }

    private static String showSql(Configuration configuration, BoundSql boundSql) {
        // 获取参数
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        // sql语句中多个空格都用一个空格代替
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");

        if (CollectionUtils.isNotEmpty(parameterMappings) && parameterObject != null) {
            // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

            // 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
            } else {
                // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();

                    if (metaObject.hasGetter(propertyName)) {
                        // 相当于从parameterObject对象(或者param1等的paramNameValuePairs)中取出propertyName对应的值
                        Object obj = metaObject.getValue(propertyName);
                        // 将sql字符串中的第1个问号(?)替换成值
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        // 该分支是动态sql
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        // 打印出缺失，提醒该参数缺失并防止错位
                        sql = sql.replaceFirst("\\?", "缺失");
                    }
                }
            }
        }

        return sql;
    }

    private static String getParameterValue(Object obj) {
        String value;

        if (obj instanceof String) {
            value = "'" + obj + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }

        return value;
    }

}
