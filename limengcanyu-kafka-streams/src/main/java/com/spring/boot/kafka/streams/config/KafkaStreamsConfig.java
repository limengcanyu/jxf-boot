package com.spring.boot.kafka.streams.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/04/04 09:33
 */
@Configuration(proxyBeanMethods = false)
@EnableKafkaStreams
public class KafkaStreamsConfig {

    @Bean
    public NewTopic ks1In() {
        return new NewTopic("ks1In", 1, (short) 1);
    }

    @Bean
    public NewTopic ks1Out() {
        return new NewTopic("ks1Out", 1, (short) 1);
    }

    @Bean
    public KStream<String, String> kStream(StreamsBuilder streamsBuilder) {
        KStream<String, String> stream = streamsBuilder.stream("ks1In");
        stream.map((k, v) -> new KeyValue<>(k, v.toUpperCase()))
                .to("ks1Out", Produced.with(Serdes.String(), new Serdes.StringSerde()));
        return stream;
    }
}
