package org.asura.consul.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope // 配置变更自动刷新
public class ConfigController {

    // 从 Consul 配置中心读取
    @Value("${app.name:默认值}")
    private String appName;

    @Value("${app.env:dev}")
    private String env;

    @GetMapping("/config")
    public String getConfig() {
        return "服务名：" + appName + "<br/>环境：" + env;
    }
}
