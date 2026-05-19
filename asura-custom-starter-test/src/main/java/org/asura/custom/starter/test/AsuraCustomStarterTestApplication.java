package org.asura.custom.starter.test;


import org.asura.custom.starter.service.AcmeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication(scanBasePackages = {"com.spring.boot.custom.starter"})
public class AsuraCustomStarterTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(AsuraCustomStarterTestApplication.class, args);
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
