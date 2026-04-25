package org.asura.fastexcel.dto;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class UserExportDTO {
    @ExcelProperty("用户ID")
    private Long userId;
    @ExcelProperty("用户名")
    private String userName;
    @ExcelProperty("手机号")
    private String phone;
    @ExcelProperty("注册时间")
    private String registerTime;
}
