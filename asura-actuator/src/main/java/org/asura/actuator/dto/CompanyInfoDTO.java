package org.asura.actuator.dto;

import lombok.Data;

@Data
public class CompanyInfoDTO {
    /**
     * 记录ID
     */
    private Long recordId;
    /**
     * 公司ID
     */
    private String companyId;
    /**
     * 公司名称
     */
    private String companyName;
}
