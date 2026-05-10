package org.asura.easyexcel.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Excel导入数据通用模型（可根据实际业务调整）
 */
@Data
public class ImportDataDTO {

    @ExcelProperty(index = 0, value = "用户名")
    private String username;

    @ExcelProperty(index = 1, value = "手机号")
    private String phone;

    @ExcelProperty(index = 2, value = "邮箱")
    private String email;

}
