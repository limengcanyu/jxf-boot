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

    @GetMapping("/greet")
    public String greet(@RequestParam(defaultValue = "World") String name) {
        return akkaService.greet(name);
    }
}