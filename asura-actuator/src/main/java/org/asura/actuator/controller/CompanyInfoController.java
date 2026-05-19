package org.asura.actuator.controller;

import org.asura.actuator.dto.CompanyInfoDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
