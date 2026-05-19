package org.asura.akka.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreetingActor extends AbstractActor {

    private static final Logger log = LoggerFactory.getLogger(GreetingActor.class);

    public static Props props() {
        return Props.create(GreetingActor.class);
    }

    public record GreetMessage(String name) {

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GreetMessage.class, message -> {
                    log.info("Received greeting request for: {}", message.name());
                    String greeting = "Hello, " + message.name() + "!";
                    sender().tell(greeting, self());
                })
                .matchAny(message -> {
                    log.warn("Received unknown message: {}", message);
                })
                .build();
    }
}