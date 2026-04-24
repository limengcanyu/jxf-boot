package com.spring.boot.resilience4j.controller;

import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/bulkhead")
@RestController
public class BulkheadController {

    @Autowired
    private BulkheadRegistry bulkheadRegistry;

    /**
     * http://localhost:8080/bulkhead/bulkhead
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/bulkhead")
    @Bulkhead(name = "bulkheadA", fallbackMethod = "fallBackByBulkhead")
    public String bulkhead() throws Exception {
        Thread.sleep(500);
        return "result";
    }

    //降级处理
    private String fallBackByBulkhead(BulkheadFullException e) {
        log.debug("并发控制器开启，拒绝访问资源 ......");

        io.github.resilience4j.bulkhead.Bulkhead bulkheadA = bulkheadRegistry.bulkhead("bulkheadA");
        io.github.resilience4j.bulkhead.Bulkhead.Metrics metrics = bulkheadA.getMetrics();

        log.debug("方法降级中：metrics[ availableConcurrentCalls: {} , maxAllowedConcurrentCalls: {} ]",
                metrics.getAvailableConcurrentCalls(), metrics.getMaxAllowedConcurrentCalls());

        return "bulkhead fallBack result";
    }

}
