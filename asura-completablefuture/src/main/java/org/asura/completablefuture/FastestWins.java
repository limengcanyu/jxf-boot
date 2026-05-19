package org.asura.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * 由谁快选谁 (AnyOf)
 */
public class FastestWins {
    public static void main(String[] args) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            var channelA = CompletableFuture.supplyAsync(() -> {
                sleep(100);
                return "Channel A";
            }, executor);

            var channelB = CompletableFuture.supplyAsync(() -> {
                sleep(50); // B 更快
                return "Channel B";
            }, executor);

            // ✅ 亮点：anyOf 配合虚拟线程，快速响应，且未完成的虚拟线程会自动回收
            CompletableFuture<Object> winner = CompletableFuture.anyOf(channelA, channelB);

            System.out.println("Winner is: " + winner.join());
        }
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { throw new RuntimeException(e); }
    }
}
