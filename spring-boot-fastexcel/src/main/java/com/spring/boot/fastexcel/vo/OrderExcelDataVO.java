package com.spring.boot.fastexcel.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * Excel读取结果整体VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderExcelDataVO<T extends ExcelFixedColumnAble, D extends ExcelDynamicColumnAble> extends ExcelDataVO<T, D> {
    /**
     * 固定列数据
     */
    private T fixedColumn;
    /**
     * 动态列数据集合（每个动态列都包含项目、阶段）
     */
    private List<D> dynamicColumns;
}

