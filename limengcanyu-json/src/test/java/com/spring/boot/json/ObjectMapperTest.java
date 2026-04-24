package com.spring.boot.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.boot.json.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/05/16 19:59
 */
@Slf4j
@SpringBootTest
public class ObjectMapperTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test() throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "rock");
        map.put("LocalDateTime", LocalDateTime.now());
        map.put("Date", new Date());

        String json = objectMapper.writeValueAsString(map);
        log.debug("json: {}", json);
    }

    @Test
    public void test2() throws JsonProcessingException {
        Person person = new Person("rock", new Date(), LocalDateTime.now(), LocalDate.now(), LocalTime.now());
        String json = objectMapper.writeValueAsString(person);
        log.debug("json: {}", json);

        person = objectMapper.readValue(json, Person.class);
        log.debug("person: {}", person);
    }
}
