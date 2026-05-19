package org.asura.akka.controller;

import org.asura.akka.service.TestCounterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 测试专用的计数器 REST API 控制器
 * 
 * <p>完全独立于生产代码，使用测试专用的服务和消息类型。
 * 
 * @author Asura Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/counter")
public class TestCounterController {

    private final TestCounterService counterService;

    public TestCounterController(TestCounterService counterService) {
        this.counterService = counterService;
    }

    @PostMapping("/{counterName}/increment")
    public ResponseEntity<Long> increment(
            @PathVariable String counterName,
            @RequestParam(defaultValue = "1") long delta) {
        long result = counterService.increment(counterName, delta);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{counterName}/increment/async")
    public CompletableFuture<ResponseEntity<Long>> incrementAsync(
            @PathVariable String counterName,
            @RequestParam(defaultValue = "1") long delta) {
        return counterService.incrementAsync(counterName, delta)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{counterName}")
    public ResponseEntity<Long> get(@PathVariable String counterName) {
        long result = counterService.get(counterName);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{counterName}/async")
    public CompletableFuture<ResponseEntity<Long>> getAsync(@PathVariable String counterName) {
        return counterService.getAsync(counterName)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/batch")
    public ResponseEntity<String> batchIncrement(@RequestBody Map<String, Long> increments) {
        counterService.batchIncrement(increments);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/{counterName}/reset")
    public ResponseEntity<Long> reset(@PathVariable String counterName) {
        long result = counterService.reset(counterName);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Long>> getAll() {
        Map<String, Long> counters = counterService.getAllCounters();
        return ResponseEntity.ok(counters);
    }
}