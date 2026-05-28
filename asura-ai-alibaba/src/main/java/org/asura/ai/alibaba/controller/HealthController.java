
package org.asura.ai.alibaba.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/health")
public class HealthController {

    /**
     * 健康检查接口
     * 
     * @return 返回服务状态、服务名称和版本信息
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "asura-ai-alibaba",
                "version", "1.0.0-SNAPSHOT"
        ));
    }
}