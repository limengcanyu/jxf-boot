package org.akuma.actuator.dto;

import lombok.Data;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/30 0030
 */
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
