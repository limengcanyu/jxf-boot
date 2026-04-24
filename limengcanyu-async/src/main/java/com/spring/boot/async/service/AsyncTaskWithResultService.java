package com.spring.boot.async.service;

import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface AsyncTaskWithResultService {
    Future<String> task1() throws InterruptedException;
    Future<String> task2() throws InterruptedException;
    Future<String> task3() throws InterruptedException;

    // 返回 ListenableFuture 类型可以对结果进行更多操作
    ListenableFuture<String> task4() throws InterruptedException;
    ListenableFuture<String> task5() throws InterruptedException;
    ListenableFuture<String> task6() throws InterruptedException;

    // 返回 CompletableFuture 类型可以对结果进行更多操作
    CompletableFuture<String> task7() throws InterruptedException, ExecutionException;
    CompletableFuture<String> task8() throws InterruptedException;
    CompletableFuture<String> task9() throws InterruptedException;
}
