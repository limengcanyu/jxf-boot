/*
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.spring.boot.kafka.utils;

import com.spring.boot.kafka.entity.Person;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Gary Russell
 * @since 2.2.7
 */
@EmbeddedKafka(
        topics = {"singleTopic1", "singleTopic2", "singleTopic3", "singleTopic4", "singleTopic5", "multiTopic1", "jsonTopic"},
        partitions = 3
)
class KafkaTestUtilsTests {

    @Test
    void testGetSingleWithMoreThatOneTopic(EmbeddedKafkaBroker broker) {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(broker);

        KafkaProducer<Integer, String> producer = new KafkaProducer<>(producerProps);
        producer.send(new ProducerRecord<>("singleTopic1", 1, "foo"));
        producer.send(new ProducerRecord<>("singleTopic2", 1, "foo"));
        producer.close();

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("ktuTests1", "false", broker);
        KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(consumerProps);
        broker.consumeFromAllEmbeddedTopics(consumer);

        ConsumerRecord<Integer, String> consumerRecord = KafkaTestUtils.getSingleRecord(consumer, "singleTopic1");
        System.out.println("consumerRecord key: " + consumerRecord.key() + " value: " + consumerRecord.value());
        assertThat(consumerRecord.key()).isEqualTo(1);
        assertThat(consumerRecord.value()).isEqualTo("foo");

        consumerRecord = KafkaTestUtils.getSingleRecord(consumer, "singleTopic2");
        System.out.println("consumerRecord key: " + consumerRecord.key() + " value: " + consumerRecord.value());
        consumer.close();
    }

    @Test
    void testGetSingleJsonRecord(EmbeddedKafkaBroker broker) {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(broker);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        Person person = new Person(1, "samuro", "上海 宝山");

        KafkaProducer<Integer, Person> producer = new KafkaProducer<>(producerProps);
        producer.send(new ProducerRecord<>("jsonTopic", 1, person));
        producer.close();

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("ktuTests1", "false", broker);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.spring.boot.kafka.entity"); // 反序列化受信任的包，必须设置
        KafkaConsumer<Integer, Person> consumer = new KafkaConsumer<>(consumerProps);
        broker.consumeFromAllEmbeddedTopics(consumer);

        ConsumerRecord<Integer, Person> consumerRecord = KafkaTestUtils.getSingleRecord(consumer, "jsonTopic");
        System.out.println("consumerRecord key: " + consumerRecord.key() + " value: " + consumerRecord.value());
        assertThat(consumerRecord.key()).isEqualTo(1);
        assertThat(consumerRecord.value()).isEqualTo(person);

        consumer.close();
    }

    @Test
    void testGetSingleWithMoreThatOneTopicRecordNotThereYet(EmbeddedKafkaBroker broker) {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(broker);

        KafkaProducer<Integer, String> producer = new KafkaProducer<>(producerProps);
        producer.send(new ProducerRecord<>("singleTopic4", 1, "foo"));

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("ktuTests2", "false", broker);
        KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(consumerProps);
        broker.consumeFromEmbeddedTopics(consumer, "singleTopic4", "singleTopic5");

        long t1 = System.currentTimeMillis();
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() ->
                KafkaTestUtils.getSingleRecord(consumer, "singleTopic5", 2000L));
        assertThat(System.currentTimeMillis() - t1).isGreaterThanOrEqualTo(2000L);

        producer.send(new ProducerRecord<>("singleTopic5", 1, "foo"));
        producer.close();

        ConsumerRecord<Integer, String> consumerRecord = KafkaTestUtils.getSingleRecord(consumer, "singleTopic4");
        System.out.println("consumerRecord key: " + consumerRecord.key() + " value: " + consumerRecord.value());
        consumerRecord = KafkaTestUtils.getSingleRecord(consumer, "singleTopic5");
        System.out.println("consumerRecord key: " + consumerRecord.key() + " value: " + consumerRecord.value());

        consumer.close();
    }

    @Test
    void testGetOneRecord(EmbeddedKafkaBroker broker) throws Exception {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(broker);

        KafkaProducer<Integer, String> producer = new KafkaProducer<>(producerProps);
        producer.send(new ProducerRecord<>("singleTopic3", 0, 1, "foo"));
        producer.close();

        ConsumerRecord<?, ?> oneRecord = KafkaTestUtils.getOneRecord(broker.getBrokersAsString(), "getOne",
                "singleTopic3", 0, false, true, 10_000L);
        assertThat(oneRecord.value()).isEqualTo("foo");

        assertThat(KafkaTestUtils.getCurrentOffset(broker.getBrokersAsString(), "getOne", "singleTopic3", 0))
                .isNotNull()
                .extracting(omd -> omd.offset())
                .isEqualTo(1L);

        oneRecord = KafkaTestUtils.getOneRecord(broker.getBrokersAsString(), "getOne",
                "singleTopic3", 0, true, true, 10_000L);
        assertThat(oneRecord.value()).isEqualTo("foo");

        assertThat(KafkaTestUtils.getCurrentOffset(broker.getBrokersAsString(), "getOne", "singleTopic3", 0))
                .isNotNull()
                .extracting(omd -> omd.offset())
                .isEqualTo(1L);
    }

    @Test
    void testMultiMinRecords(EmbeddedKafkaBroker broker) throws Exception {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(broker);

        KafkaProducer<Integer, String> producer = new KafkaProducer<>(producerProps);
        producer.send(new ProducerRecord<>("multiTopic1", 0, 1, "foo"));

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("ktuTests3", "false", broker);
        KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(consumerProps);
        broker.consumeFromAnEmbeddedTopic(consumer, "multiTopic1");

        new Thread(() -> {
            try {
                TimeUnit.MICROSECONDS.sleep(500);
                producer.send(new ProducerRecord<>("multiTopic1", 0, 1, "bar"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        ConsumerRecords<Integer, String> records = KafkaTestUtils.getRecords(consumer, 10_000L, 2);
        assertThat(records.count()).isEqualTo(2);

        producer.close();
        consumer.close();
    }


    @Test
    void testGetMultiMinJsonRecords(EmbeddedKafkaBroker broker) throws Exception {
        String topic = "jsonTopic";

        Map<String, Object> producerProps = KafkaTestUtils.producerProps(broker);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        Person person1 = new Person(1, "samuro", "上海 宝山");
        Person person2 = new Person(2, "artanis", "上海 杨浦");
        Person person3 = new Person(3, "jessica", "上海 虹口");

        KafkaProducer<Integer, Person> producer = new KafkaProducer<>(producerProps);
        producer.send(new ProducerRecord<>(topic, 0, 1, person1));

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("ktuTests3", "false", broker);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.spring.boot.kafka.entity"); // 反序列化受信任的包，必须设置

        KafkaConsumer<Integer, Person> consumer = new KafkaConsumer<>(consumerProps);
        broker.consumeFromAnEmbeddedTopic(consumer, topic);

        // 500毫秒之后再发送一条
        new Thread(() -> {
            try {
                TimeUnit.MICROSECONDS.sleep(50);
                producer.send(new ProducerRecord<>(topic, 1, 2, person2));
                producer.send(new ProducerRecord<>(topic, 2, 3, person3));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        ConsumerRecords<Integer, Person> records = KafkaTestUtils.getRecords(consumer, 10_000L, 3);
//        assertThat(records.count()).isEqualTo(2);
        records.forEach(consumerRecord -> System.out.println("consumerRecord key: " + consumerRecord.key() + " value: " + consumerRecord.value()));

        producer.close();
        consumer.close();
    }

}
