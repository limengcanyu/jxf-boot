package org.asura.akka.controller;

import org.asura.akka.service.DistributedCounterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 分布式计数器 REST API 控制器
 * 
 * <p>提供高并发的分布式计数器操作接口，支持同步和异步调用。
 * 
 * <p>生产级特性：
 * <ul>
 *   <li>异步非阻塞处理</li>
 *   <li>批量操作支持</li>
 *   <li>限流保护</li>
 *   <li>详细的错误处理</li>
 * </ul>
 * 
 * @author Asura Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/counter")
public class DistributedCounterController {

    private final DistributedCounterService counterService;

    public DistributedCounterController(DistributedCounterService counterService) {
        this.counterService = counterService;
    }

    /**
     * 增量计数器
     * <a href="http://localhost:8080/api/counter/page_view/increment?delta=100">...</a>
     *
     * @param counterName 计数器名称
     * @param delta 增量值（默认1）
     * @return 更新后的值
     */
    @PostMapping("/{counterName}/increment")
    public ResponseEntity<Long> increment(
            @PathVariable String counterName,
            @RequestParam(defaultValue = "1") long delta) {
        long result = counterService.increment(counterName, delta);
        return ResponseEntity.ok(result);
    }

    /**
     * 异步增量计数器
     * <a href="http://localhost:8080/api/counter/page_view/increment/async?delta=100">...</a>
     * @param counterName 计数器名称
     * @param delta 增量值（默认1）
     * @return CompletableFuture 包装的结果
     */
    @PostMapping("/{counterName}/increment/async")
    public CompletableFuture<ResponseEntity<Long>> incrementAsync(
            @PathVariable String counterName,
            @RequestParam(defaultValue = "1") long delta) {
        return counterService.incrementAsync(counterName, delta)
                .thenApply(ResponseEntity::ok);
    }

    /**
     * 获取计数器值
     * <a href="http://localhost:8080/api/counter/page_view">...</a>
     *
     * @param counterName 计数器名称
     * @return 计数器值
     */
    @GetMapping("/{counterName}")
    public ResponseEntity<Long> get(@PathVariable String counterName) {
        long result = counterService.get(counterName);
        return ResponseEntity.ok(result);
    }

    /**
     * 异步获取计数器值
     * <a href="http://localhost:8080/api/counter/page_view/async">...</a>
     *
     * @param counterName 计数器名称
     * @return CompletableFuture 包装的结果
     */
    @GetMapping("/{counterName}/async")
    public CompletableFuture<ResponseEntity<Long>> getAsync(@PathVariable String counterName) {
        return counterService.getAsync(counterName)
                .thenApply(ResponseEntity::ok);
    }

    /**
     * 批量增量计数器
     * <a href="http://localhost:8080/api/counter/batch">...</a>
     *
     * @param increments 计数器名称到增量的映射
     * @return 成功响应
     */
    @PostMapping("/batch")
    public ResponseEntity<String> batchIncrement(@RequestBody Map<String, Long> increments) {
        counterService.batchIncrement(increments);
        return ResponseEntity.ok("OK");
    }

    /**
     * 重置计数器
     * <a href="http://localhost:8080/api/counter/page_view/reset">...</a>
     *
     * @param counterName 计数器名称
     * @return 重置后的值（0）
     */
    @PostMapping("/{counterName}/reset")
    public ResponseEntity<Long> reset(@PathVariable String counterName) {
        long result = counterService.reset(counterName);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取所有计数器值
     * <a href="http://localhost:8080/api/counter/all">...</a>
     *
     * @return 计数器名称到值的映射
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Long>> getAll() {
        Map<String, Long> counters = counterService.getAllCounters();
        return ResponseEntity.ok(counters);
    }
}