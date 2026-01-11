package com.spring.boot.fastexcel;

import lombok.Data;

@Data
public class ExcelFixedColumnVO implements ExcelFixedColumnAble{

    private String project;

    private String stage;

    private String identity;

    private String remark;

}
