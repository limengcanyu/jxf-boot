package org.asura.completablefuture;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 复杂并行聚合（CF 的强项 + VT 的廉价）
 * 这是生产环境最典型的 Aggregator 模式。
 */
public class ComplexAggregation {
    public static void main(String[] args) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            // 模拟 10 个下游服务调用，以前这需要精打细算线程池大小
            // 现在随便开，哪怕是 1000 个也没问题
            List<CompletableFuture<String>> futures = IntStream.range(0, 10)
                    .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                        sleep(100);
                        return "Service-" + i;
                    }, executor))
                    .toList();

            // 使用 allOf 等待所有完成
            CompletableFuture<Void> allDone = CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[0])
            );

            // 结果聚合
            CompletableFuture<List<String>> resultFuture = allDone.thenApply(v ->
                    futures.stream()
                            .map(CompletableFuture::join) // 这里的 join 是安全的，因为都在等待状态
                            .collect(Collectors.toList())
            );

            System.out.println("Aggregated: " + resultFuture.join());
        }
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { throw new RuntimeException(e); }
    }
}
