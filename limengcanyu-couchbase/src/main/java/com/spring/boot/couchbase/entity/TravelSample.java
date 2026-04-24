package com.spring.boot.couchbase.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class TravelSample {
    @Id
    private String id;
    private String type;
    private String name;
    private String iata;
    private String icao;
    private String callsign;
    private String country;
}
