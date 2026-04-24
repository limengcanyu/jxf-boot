package com.spring.boot.data.redisson;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class DistributedLocksAndSynchronizersTests {

    @Autowired
    private RedissonClient redissonClient;

    ThreadPoolExecutor threadPoolExecutor;

    @BeforeEach
    void beforeEach() {
        threadPoolExecutor = new ThreadPoolExecutor(5, 5, 60, TimeUnit.SECONDS, new SynchronousQueue<>());
    }

    @AfterEach
    void afterEach() {
        threadPoolExecutor.shutdown();
        try {
            boolean termination = threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS);
            if (termination) {
                log.info("All threads terminated");
            } else {
                log.error("All threads terminated timeout");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void lock() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.submit(() -> {
                try {
                    RLock lock = redissonClient.getLock("myLock");

                    // wait for lock aquisition up to 100 seconds
                    // and automatically unlock it after 10 seconds
                    boolean res = lock.tryLock(100, 10, TimeUnit.MICROSECONDS);
                    log.debug("Thread: {} tryLock: {}", Thread.currentThread().getId(), res);
                    if (res) {
                        try {
                            log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                            TimeUnit.MICROSECONDS.sleep(new Random().nextInt(5));
                            log.debug("Thread: {} do something end", Thread.currentThread().getId());
                        } finally {
                            lock.unlock();
                            log.debug("Thread: {} unlock", Thread.currentThread().getId());
                        }
                    }
                } catch (InterruptedException e) {
                    log.error("Exception: ", e);
                }
            });
        }
    }

    @Test
    void lockAsync() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.submit(() -> {
                RLock lock = redissonClient.getLock("myLock");

                // wait for lock aquisition up to 100 seconds
                // and automatically unlock it after 10 seconds
                RFuture<Boolean> lockFuture = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);
                lockFuture.whenComplete((res, exception) -> {
                    log.debug("Thread: {} tryLockAsync: {}", Thread.currentThread().getId(), res);

                    if (res) {
                        try {
                            log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                            try {
                                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            log.debug("Thread: {} do something end", Thread.currentThread().getId());
                        } finally {
                            lock.unlockAsync();
                            log.debug("Thread: {} unlockAsync", Thread.currentThread().getId());
                        }
                    }
                });
            });
        }
    }

    @Test
    void fairLock() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    RLock lock = redissonClient.getFairLock("myLock");

                    // wait for lock aquisition up to 100 seconds
                    // and automatically unlock it after 10 seconds
                    boolean res = lock.tryLock(500, 6000, TimeUnit.MICROSECONDS);
                    log.debug("Thread: {} tryLock: {}", Thread.currentThread().getId(), res);
                    if (res) {
                        try {
                            log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                            TimeUnit.MICROSECONDS.sleep(new Random().nextInt(5));
                            log.debug("Thread: {} do something end", Thread.currentThread().getId());
                        } finally {
                            lock.unlock();
                            log.debug("Thread: {} unlock", Thread.currentThread().getId());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    void fairLockAsync() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                RLock lock = redissonClient.getFairLock("myLock");

                // wait for lock aquisition up to 100 seconds
                // and automatically unlock it after 10 seconds
                RFuture<Boolean> lockFuture = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);
                lockFuture.whenComplete((res, exception) -> {
                    log.debug("Thread: {} tryLockAsync: {}", Thread.currentThread().getId(), res);

                    if (res) {
                        try {
                            log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                            try {
                                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            log.debug("Thread: {} do something end", Thread.currentThread().getId());
                        } finally {
                            lock.unlockAsync();
                            log.debug("Thread: {} unlockAsync", Thread.currentThread().getId());
                        }
                    }
                });
            }).start();
        }

        TimeUnit.SECONDS.sleep(600);
    }

    @Test
    void multiLock() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                RLock lock1 = redissonClient.getLock("lock1");
                RLock lock2 = redissonClient.getLock("lock2");
                RLock lock3 = redissonClient.getLock("lock3");

                RLock multiLock = redissonClient.getMultiLock(lock1, lock2, lock3);

                try {
                    boolean res = multiLock.tryLock(100, 10, TimeUnit.SECONDS);
                    log.debug("Thread: {} tryLock: {}", Thread.currentThread().getId(), res);
                    if (res) {
                        try {
                            log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                            try {
                                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            log.debug("Thread: {} do something end", Thread.currentThread().getId());
                        } finally {
                            multiLock.unlock();
                            log.debug("Thread: {} unlock", Thread.currentThread().getId());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(600);
    }

    @Test
    void multiLockAsync() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                RLock lock1 = redissonClient.getLock("lock1");
                RLock lock2 = redissonClient.getLock("lock2");
                RLock lock3 = redissonClient.getLock("lock3");

                RLock multiLock = redissonClient.getMultiLock(lock1, lock2, lock3);

                RFuture<Boolean> lockFuture = multiLock.tryLockAsync(100, 10, TimeUnit.SECONDS);
                lockFuture.whenComplete((res, exception) -> {
                    log.debug("Thread: {} tryLockAsync: {}", Thread.currentThread().getId(), res);
                    if (res) {
                        try {
                            log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                            try {
                                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            log.debug("Thread: {} do something end", Thread.currentThread().getId());
                        } finally {
                            multiLock.unlockAsync();
                            log.debug("Thread: {} unlockAsync", Thread.currentThread().getId());
                        }
                    }
                });
            }).start();
        }

        TimeUnit.SECONDS.sleep(600);
    }

    @Test
    void readWriteLock() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                RReadWriteLock rwlock = redissonClient.getReadWriteLock("myLock");

                RLock lock = rwlock.readLock();

                try {
                    boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
                    log.debug("Thread: {} try read lock: {}", Thread.currentThread().getId(), res);
                    if (res) {
                        try {
                            log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                            try {
                                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            log.debug("Thread: {} do something end", Thread.currentThread().getId());
                        } finally {
                            lock.unlock();
                            log.debug("Thread: {} unlock read lock", Thread.currentThread().getId());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                RReadWriteLock rwlock = redissonClient.getReadWriteLock("myLock");

                RLock lock = rwlock.writeLock();

                try {
                    boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
                    log.debug("Thread: {} try write lock: {}", Thread.currentThread().getId(), res);
                    if (res) {
                        try {
                            log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                            try {
                                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            log.debug("Thread: {} do something end", Thread.currentThread().getId());
                        } finally {
                            lock.unlock();
                            log.debug("Thread: {} unlock write lock", Thread.currentThread().getId());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(600);
    }


    @Test
    void readWriteLockAsync() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                RReadWriteLock rwlock = redissonClient.getReadWriteLock("myLock");

                RLock lock = rwlock.readLock();

                RFuture<Boolean> lockFuture = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);
                lockFuture.whenComplete((res, exception) -> {
                    log.debug("Thread: {} try read lock async: {}", Thread.currentThread().getId(), res);
                    if (res) {
                        try {
                            log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                            try {
                                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            log.debug("Thread: {} do something end", Thread.currentThread().getId());
                        } finally {
                            lock.unlockAsync();
                            log.debug("Thread: {} unlock read lock async", Thread.currentThread().getId());
                        }
                    }
                });
            }).start();
        }

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                RReadWriteLock rwlock = redissonClient.getReadWriteLock("myLock");

                RLock lock = rwlock.writeLock();

                RFuture<Boolean> lockFuture = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);
                lockFuture.whenComplete((res, exception) -> {
                    log.debug("Thread: {} try write lock async: {}", Thread.currentThread().getId(), res);
                    if (res) {
                        try {
                            log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                            try {
                                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            log.debug("Thread: {} do something end", Thread.currentThread().getId());
                        } finally {
                            lock.unlockAsync();
                            log.debug("Thread: {} unlock write lock async", Thread.currentThread().getId());
                        }
                    }
                });
            }).start();
        }

        TimeUnit.SECONDS.sleep(600);
    }

    @Test
    void semaphore() throws InterruptedException {
        RSemaphore semaphore = redissonClient.getSemaphore("mySemaphore");
        semaphore.delete();
        semaphore.addPermits(1);

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                log.debug("Thread: {} current available permits: {}", Thread.currentThread().getId(), semaphore.availablePermits());

                try {
                    // 超过等待时间之后就不会再获取许可
                    boolean res = semaphore.tryAcquire(1, 60, TimeUnit.SECONDS);
                    log.debug("Thread: {} try acquire semaphore: {}", Thread.currentThread().getId(), res);
                    if (res) {
                        try {
                            log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                            try {
                                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            log.debug("Thread: {} do something end", Thread.currentThread().getId());
                        } finally {
                            semaphore.release();
                            log.debug("Thread: {} release semaphore", Thread.currentThread().getId());
                            log.debug("Thread: {} current available permits: {}", Thread.currentThread().getId(), semaphore.availablePermits());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(600);
    }

    @Test
    void semaphoreAsync() throws InterruptedException {
        RSemaphore semaphore = redissonClient.getSemaphore("mySemaphore");
        semaphore.delete();
        semaphore.addPermits(2);

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                RFuture<Boolean> acquireFuture = semaphore.tryAcquireAsync(1, 60, TimeUnit.SECONDS);

                acquireFuture.whenComplete((res, exception) -> {
                    log.debug("Thread: {} try acquire semaphore async: {}", Thread.currentThread().getId(), res);
                    if (res) {
                        try {
                            log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                            try {
                                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            log.debug("Thread: {} do something end", Thread.currentThread().getId());
                        } finally {
                            semaphore.releaseAsync();
                            log.debug("Thread: {} release semaphore async", Thread.currentThread().getId());
                        }
                    }
                });
            }).start();
        }

        TimeUnit.SECONDS.sleep(600);
    }

    @Test
    void permitExpirableSemaphore() throws InterruptedException {
        RPermitExpirableSemaphore semaphore = redissonClient.getPermitExpirableSemaphore("mySemaphore");
        semaphore.delete();
        semaphore.trySetPermits(1);

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    String id = semaphore.tryAcquire(60, 15, TimeUnit.SECONDS);
                    log.debug("Thread: {} try acquire semaphore async: {}", Thread.currentThread().getId(), id);
                    if (id != null) {
                        try {
                            log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                            try {
                                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            log.debug("Thread: {} do something end", Thread.currentThread().getId());
                        } finally {
                            semaphore.release(id);
                            log.debug("Thread: {} release semaphore async", Thread.currentThread().getId());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(600);
    }

    @Test
    void permitExpirableSemaphoreAsync() throws InterruptedException {
        RPermitExpirableSemaphore semaphore = redissonClient.getPermitExpirableSemaphore("mySemaphore");
        semaphore.delete();
        RFuture<Boolean> setFuture = semaphore.trySetPermitsAsync(1);

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                // 超过等待时间就不会再尝试获取许可
                RFuture<String> acquireFuture = semaphore.tryAcquireAsync(10, 15, TimeUnit.SECONDS);
                acquireFuture.whenComplete((id, exception) -> {
                    log.debug("Thread: {} try acquire semaphore async: {}", Thread.currentThread().getId(), id);
                    if (id != null) {
                        try {
                            log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                            try {
                                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            log.debug("Thread: {} do something end", Thread.currentThread().getId());
                        } finally {
                            semaphore.releaseAsync(id);
                            log.debug("Thread: {} release semaphore async", Thread.currentThread().getId());
                        }
                    }
                });
            }).start();
        }

        TimeUnit.SECONDS.sleep(600);
    }

    @Test
    void countDownLatch() throws InterruptedException {
        RCountDownLatch latch = redissonClient.getCountDownLatch("myCountDownLatch");

        latch.trySetCount(5);

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.debug("Thread: {} do something end", Thread.currentThread().getId());

                latch.countDown();
                log.debug("Thread: {} count down", Thread.currentThread().getId());
            }).start();
        }

        // await for count down
        latch.await();
        log.debug("All threads count down");
    }

    @Test
    void countDownLatchAsync() throws InterruptedException {
        RCountDownLatch latch = redissonClient.getCountDownLatch("myCountDownLatch");

        RFuture<Boolean> setFuture = latch.trySetCountAsync(5);

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.debug("Thread: {} do something end", Thread.currentThread().getId());

                latch.countDownAsync();
                log.debug("Thread: {} count down async", Thread.currentThread().getId());
            }).start();
        }

        // await for count down
        RFuture<Void> awaitFuture = latch.awaitAsync();
        log.debug("All threads count down async");

        TimeUnit.SECONDS.sleep(600);
    }

    @Test
    void spinLock() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                RLock lock = redissonClient.getSpinLock("myLock");

                try {
                    boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
                    log.debug("Thread: {} try lock: {}", Thread.currentThread().getId(), res);
                    if (res) {
                        try {
                            log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                            try {
                                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            log.debug("Thread: {} do something end", Thread.currentThread().getId());

                            // 再获取一次锁
                            boolean res2 = lock.tryLock(100, 10, TimeUnit.SECONDS);
                            log.debug("Thread: {} try lock again: {}", Thread.currentThread().getId(), res2);
                            if (res2) {
                                try {
                                    log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                                    try {
                                        TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    log.debug("Thread: {} do something end", Thread.currentThread().getId());
                                } finally {
                                    lock.unlock();
                                    log.debug("Thread: {} unlock", Thread.currentThread().getId());
                                }
                            }
                        } finally {
                            lock.unlock();
                            log.debug("Thread: {} unlock", Thread.currentThread().getId());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(600);
    }

    @Test
    void spinLockAsync() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                RLock lock = redissonClient.getSpinLock("myLock");

                RFuture<Boolean> lockFuture = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);

                lockFuture.whenComplete((res, exception) -> {
                    log.debug("Thread: {} try lock async: {}", Thread.currentThread().getId(), res);
                    if (res) {
                        try {
                            log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                            try {
                                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            log.debug("Thread: {} do something end", Thread.currentThread().getId());

                            // 再次获取锁
                            RFuture<Boolean> lockFuture2 = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);

                            lockFuture2.whenComplete((res2, exception2) -> {
                                log.debug("Thread: {} try lock async again: {}", Thread.currentThread().getId(), res2);
                                if (res2) {
                                    try {
                                        log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                                        try {
                                            TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                                        } catch (InterruptedException e) {
                                            throw new RuntimeException(e);
                                        }
                                        log.debug("Thread: {} do something end", Thread.currentThread().getId());
                                    } finally {
                                        lock.unlockAsync();
                                        log.debug("Thread: {} unlock async", Thread.currentThread().getId());
                                    }
                                }
                            });
                        } finally {
                            lock.unlockAsync();
                            log.debug("Thread: {} unlock async", Thread.currentThread().getId());
                        }
                    }
                });
            }).start();
        }

        TimeUnit.SECONDS.sleep(600);
    }

    @Test
    void fencedLock() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                RFencedLock lock = redissonClient.getFencedLock("myLock");

                Long token = lock.tryLockAndGetToken(100, 10, TimeUnit.SECONDS);
                log.debug("Thread: {} try lock and get token: {}", Thread.currentThread().getId(), token);
                if (token != null) {
                    try {
                        // 模拟业务处理
                        log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                        try {
                            TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        log.debug("Thread: {} do something end", Thread.currentThread().getId());

                        Long token2 = lock.tryLockAndGetToken();
                        log.debug("Thread: {} try lock and get token again: {}", Thread.currentThread().getId(), token2);
                        if (token2 != null) {
                            // check if token >= old token
                            if (token2 >= token) {
                                try {
                                    // 模拟业务处理
                                    log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                                    try {
                                        TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    log.debug("Thread: {} do something end", Thread.currentThread().getId());
                                } finally {
                                    lock.unlock();
                                    log.debug("Thread: {} unlock 2", Thread.currentThread().getId());
                                }
                            }
                        }
                    } finally {
                        lock.unlock();
                        log.debug("Thread: {} unlock 1", Thread.currentThread().getId());
                    }
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(600);
    }


    @Test
    void fencedLockAsync() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                RFencedLock lock = redissonClient.getFencedLock("myLock");

                RFuture<Long> lockFuture = lock.tryLockAndGetTokenAsync(100, 10, TimeUnit.SECONDS);

                long threadId = Thread.currentThread().getId();
                lockFuture.whenComplete((token, exception) -> {
                    log.debug("Thread: {} try lock and get token: {}", Thread.currentThread().getId(), token);
                    if (token != null) {
                        try {
                            // check if token >= old token
                            log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                            try {
                                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            log.debug("Thread: {} do something end", Thread.currentThread().getId());

                            // 再次获取锁
                            RFuture<Long> lockFuture2 = lock.tryLockAndGetTokenAsync(100, 10, TimeUnit.SECONDS);

                            long threadId2 = Thread.currentThread().getId();
                            lockFuture2.whenComplete((token2, exception2) -> {
                                log.debug("Thread: {} try lock and get token again: {}", Thread.currentThread().getId(), token2);
                                if (token2 != null) {
                                    try {
                                        // check if token >= old token
                                        log.debug("Thread: {} do something begin", Thread.currentThread().getId());
                                        try {
                                            TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                                        } catch (InterruptedException e) {
                                            throw new RuntimeException(e);
                                        }
                                        log.debug("Thread: {} do something end", Thread.currentThread().getId());
                                    } finally {
                                        lock.unlockAsync(threadId2);
                                        log.debug("Thread: {} unlock 2", Thread.currentThread().getId());
                                    }
                                }
                            });
                        } finally {
                            lock.unlockAsync(threadId);
                            log.debug("Thread: {} unlock 1", Thread.currentThread().getId());
                        }
                    }
                });
            }).start();
        }

        TimeUnit.SECONDS.sleep(600);
    }
}
