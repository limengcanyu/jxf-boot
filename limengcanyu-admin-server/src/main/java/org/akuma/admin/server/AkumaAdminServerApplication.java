package org.akuma.admin.server;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class AkumaAdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AkumaAdminServerApplication.class, args);
    }

}
