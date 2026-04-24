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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.csp.sentinel.util.TimeUtil;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

/**
 * https://github.com/alibaba/Sentinel/wiki/%E9%99%90%E6%B5%81---%E5%86%B7%E5%90%AF%E5%8A%A8
 *
 * 基于QPS/并发数的流量控制
 *
 * 2.2 QPS流量控制
 * 当 QPS 超过某个阈值的时候，则采取措施进行流量控制。流量控制的手段包括下面 3 种，对应 FlowRule 中的 controlBehavior 字段：
 *
 * 2. 冷启动（RuleConstant.CONTROL_BEHAVIOR_WARM_UP）方式。该方式主要用于系统长期处于低水位的情况下，当流量突然增加时，直接把系统拉升到高水位可能瞬间把系统压垮。
 * 通过"冷启动"，让通过的流量缓慢增加，在一定时间内逐渐增加到阈值上限，给冷系统一个预热的时间，避免冷系统被压垮的情况。具体的例子参见 WarmUpFlowDemo。
 *
 * When {@link FlowRule#controlBehavior} set to {@link RuleConstant#CONTROL_BEHAVIOR_WARM_UP}, real passed qps will
 * gradually increase to {@link FlowRule#count}, other than burst increasing.
 * <p/>
 * Run this demo, results are as follows:
 * <pre>
 * ...
 * 1530497805902, total:1, pass:1, block:0 // run in slow qps
 * 1530497806905, total:3, pass:3, block:0
 * 1530497807909, total:2, pass:2, block:0
 * 1530497808913, total:3, pass:3, block:0
 * 1530497809917, total:269, pass:6, block:263 // request qps burst increase, warm up behavior triggered.
 * 1530497810917, total:3676, pass:7, block:3669
 * 1530497811919, total:3734, pass:9, block:3725
 * 1530497812920, total:3692, pass:9, block:3683
 * 1530497813923, total:3642, pass:10, block:3632
 * 1530497814926, total:3685, pass:10, block:3675
 * 1530497815930, total:3671, pass:11, block:3660
 * 1530497816933, total:3660, pass:15, block:3645
 * 1530497817936, total:3681, pass:21, block:3661 // warm up process end, pass qps increased to {@link FlowRule#count}
 * 1530497818940, total:3737, pass:20, block:3716
 * 1530497819945, total:3663, pass:20, block:3643
 * 1530497820950, total:3723, pass:21, block:3702
 * 1530497821954, total:3680, pass:20, block:3660
 * ...
 * </pre>
 *
 * @author jialiang.linjl
 */
public class WarmUpFlowDemo {

    private static final String KEY = "abc";

    private static final AtomicInteger pass = new AtomicInteger();
    private static final AtomicInteger block = new AtomicInteger();
    private static final AtomicInteger total = new AtomicInteger();

    private static volatile boolean stop = false;

    private static final int threadCount = 100;
    private static int seconds = 60 + 40;

    public static void main(String[] args) throws Exception {
        initFlowRule();

        // trigger Sentinel internal init
        Entry entry = null;
        try {
            entry = SphU.entry(KEY);
        } catch (Exception e) {
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }

        // 启动统计线程
        Thread timer = new Thread(new TimerTask());
        timer.setName("sentinel-timer-task");
        timer.start();

        //first make the system run on a very low condition
        for (int i = 0; i < 3; i++) {
            Thread t = new Thread(new WarmUpTask());
            t.setName("sentinel-warmup-task");
            t.start();
        }
        Thread.sleep(20000);

        /*
         * Start more thread to simulate more qps. Since we use {@link RuleConstant.CONTROL_BEHAVIOR_WARM_UP} as
         * {@link FlowRule#controlBehavior}, real passed qps will increase to {@link FlowRule#count} in
         * {@link FlowRule#warmUpPeriodSec} seconds.
         */
        for (int i = 0; i < threadCount; i++) {
            Thread t = new Thread(new RunTask());
            t.setName("sentinel-run-task");
            t.start();
        }
    }

    private static void initFlowRule() {
        List<FlowRule> rules = new ArrayList<FlowRule>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource(KEY); // resource：资源名，即限流规则的作用对象
        rule1.setCount(20); // count: 限流阈值
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS); // grade: 限流阈值类型（QPS 或并发线程数）
        rule1.setLimitApp("default"); // limitApp: 流控针对的调用来源，若为 default 则不区分调用来源
        rule1.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP); // controlBehavior: 流量控制效果（直接拒绝、Warm Up、匀速排队）
        rule1.setWarmUpPeriodSec(10); // warmUpPeriodSec 代表期待系统进入稳定状态的时间（即预热时长）

        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
    }

    static class WarmUpTask implements Runnable {
        @Override
        public void run() {
            while (!stop) {
                Entry entry = null;

                try {
                    entry = SphU.entry(KEY);
                    // token acquired, means pass
                    pass.addAndGet(1);
                } catch (BlockException e1) {
                    block.incrementAndGet();
                } catch (Exception e2) {
                    // biz exception
                } finally {
                    total.incrementAndGet();
                    if (entry != null) {
                        entry.exit();
                    }
                }

                Random random2 = new Random();
                try {
                    TimeUnit.MILLISECONDS.sleep(random2.nextInt(2000));
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }
    }

    static class RunTask implements Runnable {
        @Override
        public void run() {
            while (!stop) {
                Entry entry = null;
                try {
                    entry = SphU.entry(KEY);
                    pass.addAndGet(1);
                } catch (BlockException e1) {
                    block.incrementAndGet();
                } catch (Exception e2) {
                    // biz exception
                } finally {
                    total.incrementAndGet();
                    if (entry != null) {
                        entry.exit();
                    }
                }

                Random random2 = new Random();
                try {
                    TimeUnit.MILLISECONDS.sleep(random2.nextInt(50));
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }
    }

    static class TimerTask implements Runnable {

        @Override
        public void run() {
            long start = System.currentTimeMillis();
//            System.out.println("begin to statistic!!!");
            System.out.println("开始统计!!!");

            long oldTotal = 0;
            long oldPass = 0;
            long oldBlock = 0;

            while (!stop) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
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

//                System.out.println(TimeUtil.currentTimeMillis() + ", total:" + oneSecondTotal
//                    + ", pass:" + oneSecondPass
//                    + ", block:" + oneSecondBlock);

                System.out.println(TimeUtil.currentTimeMillis() + ", 总数量:" + oneSecondTotal
                    + ", 通过:" + oneSecondPass
                    + ", 阻塞:" + oneSecondBlock);

                if (seconds-- <= 0) {
                    stop = true;
                }
            }

            long cost = System.currentTimeMillis() - start;
//            System.out.println("time cost: " + cost + " ms");
//            System.out.println("total:" + total.get() + ", pass:" + pass.get()
//                + ", block:" + block.get());

            System.out.println("耗时: " + cost + " ms");
            System.out.println("总数量:" + total.get() + ", 通过:" + pass.get()
                    + ", 阻塞:" + block.get());

            System.exit(0);
        }
    }
}
