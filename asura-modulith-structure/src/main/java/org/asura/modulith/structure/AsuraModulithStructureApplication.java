package org.asura.modulith.structure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AsuraModulithStructureApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(AsuraModulithStructureApplication.class, args);

        // org.asura.modulith.structure.ModulithArchitectureTest.verifyModuleArchitecture验证会失败，因为user.service没有暴露asyncPlaceOrder方法
//        UserService userService = applicationContext.getBean(UserService.class);
//        userService.asyncPlaceOrder(1L, 2);
    }

}
