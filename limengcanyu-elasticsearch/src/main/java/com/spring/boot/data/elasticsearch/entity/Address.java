package com.spring.boot.data.elasticsearch.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.geo.GeoJsonPoint;

@Document(indexName = "address")
@Data
public class Address {
    String city, street;
    GeoJsonPoint location;
}
