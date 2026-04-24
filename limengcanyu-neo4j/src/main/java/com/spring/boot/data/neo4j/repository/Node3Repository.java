package com.spring.boot.data.neo4j.repository;

import com.spring.boot.data.neo4j.entity.Node3;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface Node3Repository extends Neo4jRepository<Node3, Long> {

    Node3 findByName(String name);

}
