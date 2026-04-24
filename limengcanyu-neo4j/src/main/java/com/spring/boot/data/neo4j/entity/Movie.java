package com.spring.boot.data.neo4j.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node
public class Movie {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String released;

    public Movie() {
    }

    public Movie(String title, String released) {
        this.title = title;
        this.released = released;
    }
}
