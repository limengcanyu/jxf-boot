package com.spring.boot.data.neo4j.repository;

import com.spring.boot.data.neo4j.entity.Node1;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface Node1Repository extends Neo4jRepository<Node1, Long> {

    Optional<Node1> findByName(String name);

}
