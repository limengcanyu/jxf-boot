package org.asura.fastexcel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class OrderExcelDynamicColumnVO implements ExcelDynamicColumnAble {

    @NotBlank(message = "[零件号]必填项未填写")
    @Schema(description = "零件号", example = "6608107027")
    private String partNumber;

    @Schema(description = "用量", example = "1")
    private Integer usageQuantity;

    @NotBlank(message = "[样车编号]必填项未填写")
    @Schema(description = "样车编号", example = "")
    private String prototypeNumber;

    @Schema(description = "是否使用零件：Y/N", example = "")
    private String partUsedFlag;

    @Override
    public Map<String, String> getFixedColumnFieldNameToIndexName() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("partNumber", "partNumberColumnIndex");
        result.put("usageQuantity", "usageQuantityColumnIndex");
        return result;
    }

    @Override
    public Map<String, String> getDynamicColumnFieldName() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("head", "prototypeNumber");
        result.put("value", "partUsedFlag");
        return result;
    }

}
