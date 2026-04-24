package com.spring.boot.data.neo4j.repository;

import com.spring.boot.data.neo4j.entity.Node2;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface Node2Repository extends Neo4jRepository<Node2, Long> {

    Node2 findByName(String name);

}
