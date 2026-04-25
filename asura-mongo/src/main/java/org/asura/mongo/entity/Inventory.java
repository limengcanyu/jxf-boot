package org.asura.mongo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Inventory {

    private Date productionDate;

    private String productCode;

    private String storeCode;
}
