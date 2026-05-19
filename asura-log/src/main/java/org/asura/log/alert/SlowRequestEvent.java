package org.asura.log.alert;

import org.springframework.context.ApplicationEvent;

/**
 * 慢请求事件，用于触发告警
 */
public class SlowRequestEvent extends ApplicationEvent {
    private final String requestKey; // 如"GET:/api/user"
    private final double durationMs; // 耗时(ms)
    private final String clientIp;   // 客户端IP
    private final String params;     // 脱敏后的参数

    public SlowRequestEvent(Object source, String requestKey, double durationMs,
                            String clientIp, String params) {
        super(source);
        this.requestKey = requestKey;
        this.durationMs = durationMs;
        this.clientIp = clientIp;
        this.params = params;
    }

    // Getters
    public String getRequestKey() { return requestKey; }
    public double getDurationMs() { return durationMs; }
    public String getClientIp() { return clientIp; }
    public String getParams() { return params; }
}

