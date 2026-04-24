/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spring.boot.sentinel.flow;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 2.1 并发线程数流量控制
 * 线程数限流用于保护业务线程数不被耗尽。例如，当应用所依赖的下游应用由于某种原因导致服务不稳定、响应延迟增加，
 * 对于调用者来说，意味着吞吐量下降和更多的线程数占用，极端情况下甚至导致线程池耗尽。为应对高线程占用的情况，
 * 业内有使用隔离的方案，比如通过不同业务逻辑使用不同线程池来隔离业务自身之间的资源争抢（线程池隔离），
 * 或者使用信号量来控制同时请求的个数（信号量隔离）。这种隔离方案虽然能够控制线程数量，但无法控制请求排队时间。
 * 当请求过多时排队也是无益的，直接拒绝能够迅速降低系统压力。Sentinel线程数限流不负责创建和管理线程池，
 * 而是简单统计当前请求上下文的线程个数，如果超出阈值，新的请求会被立即拒绝。
 *
 * @author jialiang.linjl
 */
public class FlowThreadDemo {

    private static final AtomicInteger pass = new AtomicInteger();
    private static final AtomicInteger block = new AtomicInteger();
    private static final AtomicInteger total = new AtomicInteger();
    private static final AtomicInteger activeThread = new AtomicInteger();

    private static volatile boolean stop = false;
    private static final int threadCount = 10;

    private static int seconds = 10;
    private static volatile int methodBRunningTime = 2000;

    public static void main(String[] args) throws Exception {
        System.out.println(
            "MethodA will call methodB. After running for a while, methodB becomes fast, "
                + "which make methodA also become fast ");

        tick();

        initFlowRule();

        for (int i = 0; i < threadCount; i++) {
            Thread entryThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        Entry methodA = null;
                        try {
                            TimeUnit.MILLISECONDS.sleep(5);

                            methodA = SphU.entry("methodA"); // Record statistics and perform rule checking for the given resource.
                            activeThread.incrementAndGet(); // 满足规则时，活动线程递增

                            Entry methodB = SphU.entry("methodB");
                            TimeUnit.MILLISECONDS.sleep(methodBRunningTime); // 模拟方法B耗时2秒
                            methodB.exit();

                            pass.addAndGet(1); // 通过线程数量递增
                        } catch (BlockException e1) {
                            block.incrementAndGet(); // 不满足规则时，阻塞线程数量递增
                        } catch (Exception e2) {
                            // biz exception
                        } finally {
                            total.incrementAndGet(); // 总线程数量自增

                            if (methodA != null) {
                                methodA.exit(); // 模拟方法A执行完成后退出
                                activeThread.decrementAndGet(); // 活动线程递减
                            }
                        }
                    }
                }
            });
            entryThread.setName("working thread");
            entryThread.start();
        }
    }

    private static void initFlowRule() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource("methodA");
        // set limit concurrent thread for 'methodA' to 2
        rule1.setCount(2);
        rule1.setGrade(RuleConstant.FLOW_GRADE_THREAD);
        rule1.setLimitApp("default");

        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
    }

    private static void tick() {
        Thread timer = new Thread(new TimerTask());
        timer.setName("sentinel-timer-task");
        timer.start();
    }

    static class TimerTask implements Runnable {

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            System.out.println("begin to statistic!!!");

            long oldTotal = 0;
            long oldPass = 0;
            long oldBlock = 0;

            while (!stop) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 终端当前线程
                }

                long globalTotal = total.get();
                long oneSecondTotal = globalTotal - oldTotal;
                oldTotal = globalTotal;

                long globalPass = pass.get();
                long oneSecondPass = globalPass - oldPass;
                oldPass = globalPass;

                long globalBlock = block.get();
                long oneSecondBlock = globalBlock - oldBlock;
                oldBlock = globalBlock;

                System.out.print(seconds + " total qps is: " + oneSecondTotal);
                System.out.println(", currentTimeMillis: " + TimeUtil.currentTimeMillis() + ", total: " + oneSecondTotal
                    + ", pass: " + oneSecondPass + ", block: " + oneSecondBlock + " activeThread: " + activeThread.get());

                if (seconds-- <= 0) {
                    stop = true;
                }

                if (seconds == 40) {
                    System.out.println("method B is running much faster; more requests are allowed to pass");
                    methodBRunningTime = 20;
                }
            }

            long cost = System.currentTimeMillis() - start;
            System.out.print("time cost: " + cost + " ms");
            System.out.println(", total: " + total.get() + ", pass: " + pass.get() + ", block: " + block.get());

            System.exit(0);
        }
    }
}
