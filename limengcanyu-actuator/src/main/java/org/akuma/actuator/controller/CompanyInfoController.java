package org.akuma.actuator.controller;

import org.akuma.actuator.dto.CompanyInfoDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/30 0030
 */
@RestController
public class CompanyInfoController {
    @RequestMapping("/companyInfo/selectUserInfo")
    public CompanyInfoDTO selectUserInfo(){
        CompanyInfoDTO companyInfoDTO = new CompanyInfoDTO();
        companyInfoDTO.setRecordId(1L);
        companyInfoDTO.setCompanyId("C00001");
        companyInfoDTO.setCompanyName("公司C00001");

        return companyInfoDTO;
    }
}
