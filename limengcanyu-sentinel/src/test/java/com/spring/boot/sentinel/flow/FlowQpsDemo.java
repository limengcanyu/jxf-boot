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
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于QPS/并发数的流量控制
 *
 * 2.2 QPS流量控制
 * 当 QPS 超过某个阈值的时候，则采取措施进行流量控制。流量控制的手段包括下面 3 种，对应 FlowRule 中的 controlBehavior 字段：
 *
 * 1. 直接拒绝（RuleConstant.CONTROL_BEHAVIOR_DEFAULT）方式。该方式是默认的流量控制方式，当QPS超过任意规则的阈值后，新的请求就会被立即拒绝，
 * 拒绝方式为抛出FlowException。这种方式适用于对系统处理能力确切已知的情况下，比如通过压测确定了系统的准确水位时。具体的例子参见 FlowqpsDemo。
 *
 * @author jialiang.linjl
 */
public class FlowQpsDemo {

    private static final String KEY = "abc";

    private static final AtomicInteger pass = new AtomicInteger();
    private static final AtomicInteger block = new AtomicInteger();
    private static final AtomicInteger total = new AtomicInteger();

    private static volatile boolean stop = false;

    private static final int threadCount = 32;

    private static int seconds = 60 + 40;

    public static void main(String[] args) {
        initFlowQpsRule();

        tick(); // 开启统计任务

        // first make the system run on a very low condition
        simulateTraffic(); // 开始模拟流量

        System.out.println("===== begin to do flow control");
        System.out.println("only 20 requests per second can pass");

    }

    private static void initFlowQpsRule() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource(KEY); // resource：资源名，即限流规则的作用对象
        // set limit qps to 20
        rule1.setCount(20); // count: 限流阈值
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS); // grade: 限流阈值类型，QPS 或线程数
//        rule1.setStrategy(); // // strategy: 根据调用关系选择策略
        rule1.setLimitApp("default");
        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
    }

    /**
     * 模拟流量
     */
    private static void simulateTraffic() {
        // 模拟32个线程执行任务
        for (int i = 0; i < threadCount; i++) {
            Thread t = new Thread(new RunTask());
            t.setName("simulate-traffic-Task");
            t.start();
        }
    }

    /**
     * 时钟任务 统计信息
     */
    private static void tick() {
        Thread timer = new Thread(new TimerTask());
        timer.setName("sentinel-timer-task");
        timer.start();
    }

    static class TimerTask implements Runnable {

        @Override
        public void run() {
            System.out.println("execute TimerTask");

            long start = System.currentTimeMillis();
            System.out.println("begin to statistic!!!");

            long oldTotal = 0;
            long oldPass = 0;
            long oldBlock = 0;

            while (!stop) {
                // 经过1秒统计一次
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long globalTotal = total.get(); // 总请求数
                System.out.println("globalTotal: " + globalTotal);
                long oneSecondTotal = globalTotal - oldTotal;
                oldTotal = globalTotal;

                long globalPass = pass.get(); // 通过请求数
                long oneSecondPass = globalPass - oldPass;
                oldPass = globalPass;

                long globalBlock = block.get(); // 阻塞请求数
                long oneSecondBlock = globalBlock - oldBlock;
                oldBlock = globalBlock;

                System.out.print(seconds + " send qps is: " + oneSecondTotal);
                System.out.println(", currentTimeMillis:" + TimeUtil.currentTimeMillis() + ", total:" + oneSecondTotal
                    + ", pass:" + oneSecondPass + ", block:" + oneSecondBlock);

                // seconds耗尽时停止
                if (seconds-- <= 0) {
                    stop = true;
                }
            }

            long cost = System.currentTimeMillis() - start;
            System.out.print("end to statistic time cost: " + cost + " ms");
            System.out.println(", total:" + total.get() + ", pass:" + pass.get() + ", block:" + block.get());

            System.exit(0);
        }
    }

    /**
     * 线程开始执行后，先执行流量验证规则，然后休眠随机时间
     */
    static class RunTask implements Runnable {
        @Override
        public void run() {
            System.out.println("execute RunTask currentThreadID: " + Thread.currentThread().getId() + " begin");

            int loop = 0;

            while (!stop) {
                Entry entry = null;

                // 流量处理
                try {
                    entry = SphU.entry(KEY); // 记录统计并对资源进行规则检查
                    // token acquired, means pass
                    pass.addAndGet(1); // 符合规则通过
                } catch (BlockException e1) {
                    block.incrementAndGet(); // 阻塞递增
                } catch (Exception e2) {
                    // biz exception
                } finally {
                    total.incrementAndGet(); // 总量递增
                    if (entry != null) {
                        entry.exit();
                    }
                }

                // 让当前线程休眠随机时间
                Random random2 = new Random();
                int sleep = random2.nextInt(50);
//                System.out.println("RunTask sleep " + sleep + " MILLISECONDS");
                try {
                    TimeUnit.MILLISECONDS.sleep(sleep);
                } catch (InterruptedException e) {
                    // ignore
                }

                loop++;
            }

//            System.out.println("execute times: " + loop);
            System.out.println("execute RunTask currentThreadID: " + Thread.currentThread().getId() + " end");

        }
    }
}
