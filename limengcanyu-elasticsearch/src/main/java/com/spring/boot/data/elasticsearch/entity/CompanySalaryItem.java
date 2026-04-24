package com.spring.boot.data.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/05/16 19:28
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanySalaryItem {
    private String itemCode;
    private String itemName;
}
