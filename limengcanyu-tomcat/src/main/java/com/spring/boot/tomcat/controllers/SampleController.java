package com.spring.boot.tomcat.controllers;

import com.spring.boot.tomcat.annotations.RolePermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class SampleController {

    /**
     * http://localhost:8070/echo
     *
     * @return
     */
    @RolePermission(required = false, roles = {"ROLE1", "ROLE2"})
    @RequestMapping("/echo")
    public String echo() {
        log.debug("this is tomcat app echo");
        return "this is tomcat app";
    }

    @RequestMapping("/getMap")
    public Map<String, Object> getMap(@RequestBody Map<String, Object> map) {
        System.out.println("getMap param: " + map);
        map.put("add", 123);
        return map;
    }

}
