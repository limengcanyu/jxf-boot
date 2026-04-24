package com.spring.boot.data.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/05/16 19:19
 */
@Document(indexName="salary_compute_item")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SalaryComputeItem {

    private String tenantId;
    private String companyId;
    private String salaryMonth;
    private Integer salaryBatch;
    private List<CompanySalaryItem> salaryItemList;
}
