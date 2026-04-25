package org.asura.neo4j.repository;

import org.asura.neo4j.entity.City;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CityRepository extends Neo4jRepository<City, Long> {

    City findOneByNameAndState(String name, String state);

}
