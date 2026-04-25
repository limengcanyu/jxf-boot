package org.asura.validation;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class AsuraValidationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsuraValidationApplication.class, args);
    }

    /**
     * http://localhost:8080/validate
     *
     * Controller层校验
     *
     * @param pserson
     * @return
     */
    @RequestMapping("/validate")
    public String validate(@Valid @RequestBody Person pserson) {
        return "参数校验成功";
    }

    @Autowired
    private PersonService personService;

    /**
     * http://localhost:8080/save
     *
     * 非Controller层校验
     *
     * @return
     */
    @RequestMapping("/save")
    public String save() {
        return personService.save(new Person());
    }

}
