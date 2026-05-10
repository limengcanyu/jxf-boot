package org.asura.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class Java21Async {

    public static void main(String[] args) throws Exception {
        // 虚拟线程执行器，无限容量，用完即销毁，无需调优
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            long start = System.currentTimeMillis();

            // 将 executor 传入 supplyAsync
            var future1 = CompletableFuture.supplyAsync(() -> {
                System.out.println("Task 1 running on: " + Thread.currentThread());
                sleep(50);
                return "Product Info";
            }, executor); // <--- 关键点在这里

            var future2 = CompletableFuture.supplyAsync(() -> {
                System.out.println("Task 2 running on: " + Thread.currentThread());
                sleep(80);
                return "Stock Info";
            }, executor);

            var result = future1.thenCombine(future2, (p, s) -> p + " & " + s).join();

            System.out.println("Result: " + result);
            System.out.println("Cost: " + (System.currentTimeMillis() - start) + "ms");
        }
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { throw new RuntimeException(e); }
    }
}
