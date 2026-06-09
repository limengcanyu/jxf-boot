package org.asura.custom.starter.test;


import jakarta.annotation.Resource;
import org.asura.custom.starter.service.AcmeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class AsuraCustomStarterTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(AsuraCustomStarterTestApplication.class, args);
    }

    @Resource
    private AcmeService acmeService;

    /**
     * <a href="http://localhost:8080/print">...</a>
     *
     */
    @GetMapping("/print")
    public String print(){
        return acmeService.print();
    }
}
