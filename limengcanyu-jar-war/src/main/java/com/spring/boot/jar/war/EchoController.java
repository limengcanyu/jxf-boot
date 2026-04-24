package com.spring.boot.jar.war;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {
    /**
     * localhost:8080/echo
     *
     * @return
     */
    @RequestMapping("/echo")
    public String echo() {
        return "hello";
    }
}
