package com.spring.boot.async.service;

import java.util.concurrent.CompletableFuture;

public interface AsyncTaskWithResultOtherService {
    CompletableFuture<String> task7() throws InterruptedException;
    CompletableFuture<String> task8() throws InterruptedException;
    CompletableFuture<String> task9() throws InterruptedException;
}
