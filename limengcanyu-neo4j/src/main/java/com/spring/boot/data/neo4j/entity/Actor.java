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
public class Actor {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String born;

    public Actor() {
    }

    public Actor(String name, String born) {
        this.name = name;
        this.born = born;
    }

    @Relationship(type = "LIVED_IN", direction = Relationship.Direction.INCOMING)
    public Set<City> cities;

    public void addCity(City city) {
        if (CollectionUtils.isEmpty(cities)) {
            cities = new HashSet<>();
        }
        cities.add(city);
    }

    @Relationship(type = "ACTED_IN", direction = Relationship.Direction.OUTGOING)
    public Set<Movie> actors;

    public void addActor(Movie movie) {
        if (CollectionUtils.isEmpty(actors)) {
            actors = new HashSet<>();
        }
        actors.add(movie);
    }

    @Relationship(type = "DIRECTED", direction = Relationship.Direction.OUTGOING)
    public Set<Movie> directors;

    public void addDirector(Movie movie) {
        if (CollectionUtils.isEmpty(directors)) {
            directors = new HashSet<>();
        }
        directors.add(movie);
    }

}
