package com.spring.boot.data.elasticsearch.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Data
@Document(indexName = "person")
public class Person {
    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String createTime;

    List<Person> friends;

    Map<String, Address> knownLocations;

    @Version
    private Long version;

    public Person(String firstName, String lastName, String createTime) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.createTime = createTime;
    }
}
