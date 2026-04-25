package org.asura.log.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 测试请求监控功能的控制器
 * 包含各种场景的接口，用于验证耗时统计、参数脱敏、慢请求告警等功能
 *
 * 指标收集验证
 * http://localhost:8080/actuator/prometheus
 */
@RestController
@RequestMapping("/api/test")
public class TestMonitorController {

    /**
     * 基础GET接口测试
     * http://localhost:8080/api/test/basic
     */
    @GetMapping("/basic")
    public Map<String, String> basicGet() {
        Map<String, String> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "基础GET接口测试");
        return result;
    }

    /**
     * 带参数的GET接口（测试参数记录与脱敏）
     * http://localhost:8080/api/test/with-params?name=test&age=20&mobile=13800138000&password=123456
     */
    @GetMapping("/with-params")
    public Map<String, Object> getWithParams(
            @RequestParam String name,
            @RequestParam Integer age,
            @RequestParam(required = false) String mobile,    // 敏感参数（手机号）
            @RequestParam(required = false) String password) { // 敏感参数（密码）

        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("receivedParams", "name=" + name + ", age=" + age);
        // 不返回敏感参数，仅用于测试输入参数的脱敏记录
        return result;
    }

    /**
     * POST接口测试（测试表单参数）
     */
    @PostMapping("/form-data")
    public Map<String, Object> formData(
            @RequestParam String username,
            @RequestParam String idcard) {  // 敏感参数（身份证号）

        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "表单数据接收成功");
        return result;
    }

    /**
     * JSON请求测试
     */
    @PostMapping("/json-body")
    public Map<String, Object> jsonBody(@RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("receivedFields", body.keySet());
        return result;
    }

    /**
     * 可控制耗时的接口（测试慢请求告警）
     * http://localhost:8080/api/test/delay?delayMs=600
     *
     * @param delayMs 延迟时间（毫秒），用于模拟不同耗时的操作
     */
    @GetMapping("/delay")
    public Map<String, String> delayOperation(@RequestParam(defaultValue = "100") int delayMs) {
        try {
            // 模拟耗时操作
            TimeUnit.MILLISECONDS.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Map<String, String> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "延迟" + delayMs + "毫秒后完成");
        return result;
    }

    /**
     * 模拟异常的接口（测试异常请求统计）
     * http://localhost:8080/api/test/with-exception?throwException=true
     */
    @GetMapping("/with-exception")
    public void exceptionTest(@RequestParam(required = false) boolean throwException) {
        if (throwException) {
            throw new RuntimeException("测试异常情况的请求统计");
        }
    }
}

