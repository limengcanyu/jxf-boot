package com.spring.boot.data.neo4j.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node
public class Node3 {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Node3() {
    }

    public Node3(String name) {
        this.name = name;
    }

}
