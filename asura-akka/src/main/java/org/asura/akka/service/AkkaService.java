package org.asura.akka.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.asura.akka.actor.GreetingActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

@Service
public class AkkaService {

    private static final Logger log = LoggerFactory.getLogger(AkkaService.class);

    private final ActorSystem actorSystem;
    private ActorRef greetingActor;

    public AkkaService(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    @PostConstruct
    public void init() {
        greetingActor = actorSystem.actorOf(GreetingActor.props(), "greeting-actor");
        log.info("GreetingActor created with path: {}", greetingActor.path());
    }

    @PreDestroy
    public void destroy() {
        log.info("Shutting down Akka system");
        actorSystem.terminate();
    }

    public String greet(String name) {
        FiniteDuration timeoutDuration = Duration.create(5, TimeUnit.SECONDS);
        Timeout timeout = new Timeout(timeoutDuration);
        Future<Object> future = Patterns.ask(greetingActor, new GreetingActor.GreetMessage(name), timeout);
        
        try {
            return (String) Await.result(future, timeoutDuration);
        } catch (Exception e) {
            log.error("Error sending message to GreetingActor", e);
            return "Error: " + e.getMessage();
        }
    }
}