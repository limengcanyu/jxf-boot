package com.spring.boot.fastexcel.vo;


import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

/**
 * Excel读取结果整体VO
 */
@Data
public class ExcelDataVO<T extends ExcelFixedColumnAble, D extends ExcelDynamicColumnAble> {
    /**
     * 固定列数据
     */
    @Valid
    private T fixedColumn;
    /**
     * 动态列数据集合（每个动态列都包含项目、阶段）
     */
    @Valid
    private List<D> dynamicColumns;
    /**
     * 行号（用于定位Excel错误行）
     */
    private int rowNum;
}

