package com.spring.boot.mongo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "product")
public class Product {

    @Id
    String id;

    String batch;

    String productCode;

    String productName;

    Date expireDate;
}
