package com.spring.boot.logstash;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LogstashController {

    /**
     * http://localhost:8080/printLog
     *
     * @return
     */
    @RequestMapping("/printLog")
    public String printLog() {
        log.debug("输出一条日志记录 ......");
        return "";
    }
}
