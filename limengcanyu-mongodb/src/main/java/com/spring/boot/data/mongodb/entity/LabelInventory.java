package com.spring.boot.data.mongodb.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ExcelIgnoreUnannotated
@FieldNameConstants
@Document("label_inventory")
public class LabelInventory extends BaseEntity {

    @Id
    private String id;

    private String productCode;

    private String storeCode;

    private BigDecimal quantity;

    private Date productionDate;

    private String storageConditions;

    private String batch;

}
