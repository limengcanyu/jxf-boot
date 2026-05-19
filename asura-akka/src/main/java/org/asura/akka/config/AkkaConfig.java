package org.asura.akka.config;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AkkaConfig {

    @Bean
    public ActorSystem actorSystem() {
        Config config = ConfigFactory.load();
        return ActorSystem.create("asura-akka-system", config);
    }

}