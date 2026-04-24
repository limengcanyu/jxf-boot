package com.spring.boot.undertow;

import com.spring.boot.undertow.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class EchoController {

    @Autowired
    private OrderService orderService;

    /**
     * localhost:8071/echo
     *
     * @return
     */
    @RequestMapping("/echo")
    public String echo() {
        log.debug("this is undertow app echo currentTimeMillis: {}", System.currentTimeMillis());
        return "this is undertow app";
    }

    /**
     * localhost:8071/getOrderNumber
     *
     * @return
     */
    @RequestMapping("/getOrderNumber")
    public String getOrderNumber(){
        log.debug("this is undertow app getOrderNumber");
        return orderService.getOrderNumber();
    }
}
