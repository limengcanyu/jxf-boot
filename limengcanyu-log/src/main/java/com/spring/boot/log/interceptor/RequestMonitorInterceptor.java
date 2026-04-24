package com.spring.boot.log.interceptor;

import com.spring.boot.log.alert.SlowRequestEvent;
import com.spring.boot.log.metrics.RequestMetrics;
import com.spring.boot.log.util.SensitiveDataUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 请求监控拦截器，集成耗时统计、指标收集、告警触发
 */
public class RequestMonitorInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(RequestMonitorInterceptor.class);
    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

    private final long slowThresholdMs; // 慢请求阈值
    private final RequestMetrics requestMetrics;
    private final ApplicationContext applicationContext;

    public RequestMonitorInterceptor(long slowThresholdMs,
                                     RequestMetrics requestMetrics,
                                     ApplicationContext applicationContext) {
        this.slowThresholdMs = slowThresholdMs;
        this.requestMetrics = requestMetrics;
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 记录请求开始时间
        START_TIME.set(System.nanoTime());
        // 增加活跃请求数
        requestMetrics.incrementActiveRequests();
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        try {
            Long startTime = START_TIME.get();
            if (startTime == null) return;

            // 计算耗时
            long endTime = System.nanoTime();
            double costMs = (double) (endTime - startTime) / TimeUnit.MILLISECONDS.toNanos(1);

            // 构建请求标识
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String requestKey = method + ":" + uri;
            String clientIp = SensitiveDataUtils.getClientIp(request);

            // 收集请求参数（已脱敏）
            String params = getRequestParams(request);

            // 记录指标
            requestMetrics.recordRequest(requestKey, costMs, ex != null);

            // 日志输出
            logRequest(method, uri, response.getStatus(), costMs, ex, clientIp, params);

            // 慢请求告警
            if (costMs > slowThresholdMs) {
                applicationContext.publishEvent(new SlowRequestEvent(
                        this, requestKey, costMs, clientIp, params
                ));
            }
        } finally {
            // 清理资源
            START_TIME.remove();
            requestMetrics.decrementActiveRequests();
        }
    }

    /**
     * 获取并脱敏请求参数
     */
    private String getRequestParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();

        // 获取GET参数
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            params.put(name, request.getParameter(name));
        }

        // 这里可以扩展处理POST请求体参数（需注意流只能读取一次的问题）
        return SensitiveDataUtils.desensitize(params.toString());
    }

    /**
     * 打印请求日志
     */
    private void logRequest(String method, String uri, int status, double costMs,
                            Exception ex, String ip, String params) {
        // 先将耗时格式化为保留2位小数的字符串
        String formattedCostMs = String.format("%.2f", costMs);

        String logFormat = "请求详情 [{} {}] 状态:{} IP:{} 耗时:{}ms 参数:{}";
        if (ex != null) {
            log.warn(logFormat + " 异常:{}", method, uri, status, ip, formattedCostMs, params, ex.getMessage());
        } else {
            log.info(logFormat, method, uri, status, ip, formattedCostMs, params);
        }
    }
}

