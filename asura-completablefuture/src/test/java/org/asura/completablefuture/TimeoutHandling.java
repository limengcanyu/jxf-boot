package org.asura.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 优雅的超时处理
 */
public class TimeoutHandling {
    public static void main(String[] args) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            CompletableFuture<String> unsafeTask = CompletableFuture.supplyAsync(() -> {
                        sleep(2000); // 模拟慢服务，耗时 2s
                        return "Slow Response";
                    }, executor)
                    // ✅ 亮点：原生支持超时，无需额外的定时任务线程池介入太深
                    .orTimeout(500, TimeUnit.MILLISECONDS)
                    .exceptionally(ex -> {
                        System.err.println("Task timed out: " + ex.getMessage());
                        return "Fallback Value"; // 降级处理
                    });

            System.out.println("Result: " + unsafeTask.join());
        }
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { throw new RuntimeException(e); }
    }
}
