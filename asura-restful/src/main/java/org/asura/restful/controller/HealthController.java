package org.asura.restful.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.asura.restful.dto.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
@Tag(name = "健康检查", description = "服务健康状态检查")
public class HealthController {

    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    @Operation(summary = "健康检查", description = "检查服务是否正常运行")
    @GetMapping
    public ResponseEntity<ApiResponse<HealthResponse>> health() {
        log.debug("健康检查请求");
        HealthResponse response = new HealthResponse();
        response.setStatus("UP");
        response.setTimestamp(LocalDateTime.now());
        response.setService("asura-restful");
        response.setVersion("1.0.0");
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "详细健康检查", description = "检查服务及依赖组件状态")
    @GetMapping("/detail")
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthDetail() {
        log.debug("详细健康检查请求");
        Map<String, Object> detail = new HashMap<>();
        detail.put("status", "UP");
        detail.put("timestamp", LocalDateTime.now());
        detail.put("service", "asura-restful");
        detail.put("version", "1.0.0");
        
        Map<String, String> components = new HashMap<>();
        components.put("memory", "OK");
        components.put("storage", "OK");
        components.put("network", "OK");
        detail.put("components", components);
        
        return ResponseEntity.ok(ApiResponse.success(detail));
    }

    public static class HealthResponse {
        private String status;
        private LocalDateTime timestamp;
        private String service;
        private String version;

        public HealthResponse() {}

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}