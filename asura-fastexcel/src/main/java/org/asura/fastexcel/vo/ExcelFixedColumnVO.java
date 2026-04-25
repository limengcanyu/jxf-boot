package org.asura.fastexcel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Excel固定列数据VO
 */
@Data
public class ExcelFixedColumnVO implements ExcelFixedColumnAble {
    /** 项目 */
    @NotBlank(message = "项目为必填项，未填写")
    @Schema(description = "项目", example = "1024")
    private String project;
    /** 阶段 */
    @NotBlank(message = "阶段为必填项，未填写")
    @Schema(description = "阶段", example = "1024")
    private String stage;
    /** 标识 */
    @NotBlank(message = "标识为必填项，未填写")
    @Schema(description = "标识", example = "1024")
    private String identifier;
    /** 备注 */
    @Schema(description = "备注", example = "1024")
    private String remark;

    @Override
    public Map<String, String> getHeadToIndexName() {
        Map<String, String>  result = new LinkedHashMap<>();
        result.put("项目", "projectColumnIndex");
        result.put("阶段", "stageColumnIndex");
        result.put("标识", "identifierColumnIndex");
        result.put("备注", "remarkColumnIndex");
        return result;
    }

    @Override
    public Map<String, Integer> getIndexNameToIndexValue() {
        Map<String, Integer>  result = new LinkedHashMap<>();

        Map<String, String> headToIndexName = getHeadToIndexName();
        headToIndexName.forEach( (key,value) -> {
            result.put(value, -1);
        });

        return result;
    }

    @Override
    public Map<String, String> getIndexNameToHead() {
        Map<String, String>  result = new LinkedHashMap<>();

        Map<String, String> headToIndexName = getHeadToIndexName();
        headToIndexName.forEach( (key,value) -> {
            result.put(value, key);
        });

        return result;
    }

    @Override
    public Map<String, String> getFieldNameToIndexName() {
        Map<String, String>  result = new LinkedHashMap<>();
        result.put("project", "projectColumnIndex");
        result.put("stage", "stageColumnIndex");
        result.put("identifier", "identifierColumnIndex");
        result.put("remark", "remarkColumnIndex");
        return result;
    }
}

