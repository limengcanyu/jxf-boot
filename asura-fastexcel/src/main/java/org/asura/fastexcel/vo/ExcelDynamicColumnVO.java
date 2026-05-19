package org.asura.fastexcel.vo;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Excel动态列数据VO（包含所属项目、阶段）
 */
@Data
public class ExcelDynamicColumnVO implements ExcelDynamicColumnAble {
    /**
     * 所属项目（关联固定列）
     */
    @NotBlank(message = "项目为必填项，未填写")
    private String project;
    /**
     * 所属阶段（关联固定列）
     */
    @NotBlank(message = "阶段为必填项，未填写")
    private String stage;
    /**
     * 动态列名称（如biw001、test001等）
     */
    @NotBlank(message = "动态列名称为必填项，未填写")
    private String columnName;
    /**
     * 动态列对应的值
     */
    private String columnValue;

    @Override
    public Map<String, String> getFixedColumnFieldNameToIndexName() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("project", "projectColumnIndex");
        result.put("stage", "stageColumnIndex");
        return result;
    }

    @Override
    public Map<String, String> getDynamicColumnFieldName() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("head", "columnName");
        result.put("value", "columnValue");
        return result;
    }


}
