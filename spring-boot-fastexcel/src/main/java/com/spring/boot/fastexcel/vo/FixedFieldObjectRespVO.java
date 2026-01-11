package com.spring.boot.fastexcel.vo;


import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ExcelIgnoreUnannotated
@Data
public class FixedFieldObjectRespVO {

    @ExcelProperty(value = "项目")
    private String project;

    @ExcelProperty(value = "阶段")
    private String stage;

    @ExcelProperty(value = "标识")
    private String identifier;

    public static List<FixedFieldObjectRespVO> getFixedFieldObjectRespVOList() {
        List<FixedFieldObjectRespVO> fixedFieldObjectRespVOList = new ArrayList<>();

        FixedFieldObjectRespVO fixedFieldObjectRespVO = new FixedFieldObjectRespVO();
        fixedFieldObjectRespVO.setProject("project001");
        fixedFieldObjectRespVO.setStage("stage001");
        fixedFieldObjectRespVO.setIdentifier("bli001");
        fixedFieldObjectRespVOList.add(fixedFieldObjectRespVO);

        //====================================================================================
        fixedFieldObjectRespVO = new FixedFieldObjectRespVO();
        fixedFieldObjectRespVO.setProject("project002");
        fixedFieldObjectRespVO.setStage("stage002");
        fixedFieldObjectRespVO.setIdentifier("bli002");
        fixedFieldObjectRespVOList.add(fixedFieldObjectRespVO);

        return fixedFieldObjectRespVOList;
    }

}

