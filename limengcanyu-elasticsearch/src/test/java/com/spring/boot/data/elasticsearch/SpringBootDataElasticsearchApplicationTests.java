package com.spring.boot.data.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.spring.boot.data.elasticsearch.entity.Address;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.geo.GeoJsonPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class SpringBootDataElasticsearchApplicationTests {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    ElasticsearchClient elasticsearchClient;

    @Autowired
    RestClient restClient;

    @Test
    void contextLoads() {
        Person person = new Person();
        person.setId("cb7bef");
        person.setFirstname("Sarah");
        person.setLastname("Connor");

        Address address = new Address();
        address.setCity("Los Angeles");
        address.setStreet("2800 East Observatory Road");
        address.setLocation(GeoJsonPoint.of(-118.3026284, 34.118347));

        List<Person> friends = new ArrayList<>();
        Person friend = new Person();
        friend.setFirstname("Kyle");
        friend.setLastname("Reese");
        friends.add(friend);

        person.setFriends(friends);

        Map<String, Address> knownLocations = new HashMap<>();

        Address knownLocation = new Address();
        knownLocation.setCity("Los Angeles");
        knownLocation.setStreet("2800 East Observatory Road");
        knownLocation.setLocation(GeoJsonPoint.of(-118.3026284, 34.118347));

        knownLocations.put("arrivedAt", knownLocation);

        person.setKnownLocations(knownLocations);

        Person savedEntity = elasticsearchOperations.save(person);
        System.out.println(savedEntity);

        person = elasticsearchOperations.get("cb7bef", Person.class);
        System.out.println(person);
    }

}
