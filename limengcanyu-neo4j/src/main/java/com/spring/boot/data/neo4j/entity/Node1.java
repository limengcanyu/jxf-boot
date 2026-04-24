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
public class Node1 {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Node1() {
    }

    public Node1(String name) {
        this.name = name;
    }

    @Relationship(type = "REFERENCE_RELATIONSHIP")
    public Set<Node2> node2s;

    public void addNode2(Node2 node2) {
        if (CollectionUtils.isEmpty(node2s)) {
            node2s = new HashSet<>();
        }
        node2s.add(node2);
    }

}
