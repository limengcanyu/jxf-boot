package org.asura.akka.controller;

import org.asura.akka.service.AkkaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AkkaController {

    private final AkkaService akkaService;

    public AkkaController(AkkaService akkaService) {
        this.akkaService = akkaService;
    }

    /**
     * 问候消息处理接口
     * <a href="http://localhost:8080/greet?name=AsuraTeam">...</a>
     *
     * @param name 问候对象的名称，默认值为 "World"
     * @return 问候语，格式为 "Hello, AsuraTeam!"
     */
    @GetMapping("/greet")
    public String greet(@RequestParam(defaultValue = "World") String name) {
        return akkaService.greet(name);
    }
}