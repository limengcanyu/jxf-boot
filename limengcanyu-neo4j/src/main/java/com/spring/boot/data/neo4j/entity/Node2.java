package com.spring.boot.data.neo4j.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Data
@Node
public class Node2 {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Node2() {
    }

    public Node2(String name) {
        this.name = name;
    }

    @Relationship(type = "REFERENCE_RELATIONSHIP")
    public Set<Node3> node3s;

    public void addNode3(Node3 node3) {
        if (CollectionUtils.isEmpty(node3s)) {
            node3s = new HashSet<>();
        }
        node3s.add(node3);
    }

}
