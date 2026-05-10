package org.asura.fastexcel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class OrderExcelFixedColumnVO implements ExcelFixedColumnAble {

    @NotNull(message = "[序号]必填项未填写")
    @Schema(description = "序号", example = "5")
    private Long serialNumber;

    @NotBlank(message = "[PSS]必填项未填写")
    @Schema(description = "PSS", example = "050")
    private String pss;

    @NotBlank(message = "[FG]必填项未填写")
    @Schema(description = "FG", example = "2600")
    private String fg;

    @NotBlank(message = "[POS]必填项未填写")
    @Schema(description = "POS", example = "020")
    private String pos;

    @Schema(description = "层级", example = "1")
    private Integer level;

    @NotEmpty(message = "[零件号]必填项未填写")
    @Schema(description = "零件号", example = "6608107027")
    private String partNumber;

    @NotBlank(message = "[零件英文名称]必填项未填写")
    @Schema(description = "零件英文名称", example = "EXPANSION TANK ASSY")
    private String partEnglishName;

    @NotBlank(message = "[零件中文名称]必填项未填写")
    @Schema(description = "零件中文名称", example = "膨胀水壶总成")
    private String partChineseName;

    @NotNull(message = "[用量]必填项未填写")
    @Schema(description = "用量", example = "1")
    private Integer usageQuantity;

    @Schema(description = "全配置BOM-车辆零件列表", example = "")
    private List<OrderExcelDynamicColumnVO> vehiclePartsList;

    @Override
    public Map<String, String> getHeadToIndexName() {
        Map<String, String>  result = new LinkedHashMap<>();
        result.put("序号", "serialNumberColumnIndex");
        result.put("PSS", "pssColumnIndex");
        result.put("FG", "fgColumnIndex");
        result.put("POS", "posColumnIndex");
        result.put("层级", "levelColumnIndex");
        result.put("零件号", "partNumberColumnIndex");
        result.put("零件英文名称", "partEnglishNameColumnIndex");
        result.put("零件中文名称", "partChineseNameColumnIndex");
        result.put("用量", "usageQuantityColumnIndex");
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
        result.put("serialNumber", "serialNumberColumnIndex");
        result.put("pss", "pssColumnIndex");
        result.put("fg", "fgColumnIndex");
        result.put("pos", "posColumnIndex");
        result.put("level", "levelColumnIndex");
        result.put("partNumber", "partNumberColumnIndex");
        result.put("partEnglishName", "partEnglishNameColumnIndex");
        result.put("partChineseName", "partChineseNameColumnIndex");
        result.put("usageQuantity", "usageQuantityColumnIndex");
        return result;
    }

}
