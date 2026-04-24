package com.spring.boot.custom.starter.test;


import com.spring.boot.custom.starter.service.AcmeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication(scanBasePackages = {"com.spring.boot.custom.starter"})
public class SpringBootCustomStarterTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootCustomStarterTestApplication.class, args);
    }

    @Autowired
    private AcmeService acmeService;

    /**
     * http://localhost:8080/print
     *
     * @return
     */
    @GetMapping("/print")
    public String print(){
        return acmeService.print();
    }
}
