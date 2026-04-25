package org.asura.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class AsuraWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsuraWebfluxApplication.class, args);
    }

    @RequestMapping("")
    public String echo() {
        return "echo service";
    }
}
