package org.asura.fastexcel.utils;


import cn.hutool.json.JSONUtil;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import org.asura.fastexcel.vo.ExcelDataVO;
import org.asura.fastexcel.vo.ExcelDynamicColumnAble;
import org.asura.fastexcel.vo.ExcelFixedColumnAble;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Excel数据泛型解析类
 *
 * @param <T> 固定列VO
 * @param <D> 动态列VO
 */
@Slf4j
public class ExcelDataGenericAnalysisEventListener<T extends ExcelFixedColumnAble, D extends ExcelDynamicColumnAble> extends AnalysisEventListener<Map<Integer, String>> {
    // 所有列表头映射：key=列索引，value=列名称 存放所有列的 例：1, "项目"
    private final Map<Integer, String> ALL_COLUMN_INDEX_VALUE_TO_HEAD = new HashMap<>();

    // 固定列表头映射：key=列名称，value=列索引 例："项目", "projectColumnIndex"
    private final Map<String, String> FIXED_COLUMN_HEAD_TO_INDEX_NAME;
    // 固定列名称（需与Excel列名完全一致，区分大小写）
    private final Set<String> FIXED_COLUMNS;
    // 固定列表头映射：key=列名称，value=列索引 例："projectColumnIndex", 1
    private final Map<String, Integer> FIXED_COLUMN_INDEX_NAME_TO_INDEX_VALUE;
    // 固定列表头映射：key=列索引名称，value=列名称 例："projectColumnIndex", "项目"
    private final Map<String, String> FIXED_COLUMN_INDEX_NAME_TO_HEAD;
    // 固定列表头映射：key=对象字段名称，value=列名称 例："project", "projectColumnIndex"
    private final Map<String, String> FIXED_COLUMN_FIELD_NAME_TO_INDEX_NAME;

    // 动态列索引名称列表（排除固定列）
    private final List<Integer> DYNAMIC_COLUMN_INDEX_NAME = new ArrayList<>();

    // 动态列表头映射：key=对象字段名称，value=列名称 例："project", "projectColumnIndex"
    private final Map<String, String> DYNAMIC_COLUMN_FIXED_FIELD_NAME_TO_INDEX_NAME;
    private final Map<String, String> DYNAMIC_COLUMN_DYNAMIC_FIELD_NAME_TO_INDEX_NAME;

    // 存储最终解析结果
    private final List<ExcelDataVO<T, D>> resultList;

    // 固定列VO类
    Class<? extends ExcelFixedColumnAble> fixedColumnVOClass;
    // 动态列VO类
    Class<? extends ExcelDynamicColumnAble> dynamicColumnVOClass;

    public ExcelDataGenericAnalysisEventListener(List<ExcelDataVO<T, D>> resultList, T fixedColumnAble, D dynamicColumnAble) {
        this.resultList = resultList;
        this.FIXED_COLUMN_HEAD_TO_INDEX_NAME = fixedColumnAble.getHeadToIndexName();
        this.FIXED_COLUMNS = FIXED_COLUMN_HEAD_TO_INDEX_NAME.keySet();
        this.FIXED_COLUMN_INDEX_NAME_TO_INDEX_VALUE = fixedColumnAble.getIndexNameToIndexValue();
        this.FIXED_COLUMN_INDEX_NAME_TO_HEAD = fixedColumnAble.getIndexNameToHead();
        this.FIXED_COLUMN_FIELD_NAME_TO_INDEX_NAME = fixedColumnAble.getFieldNameToIndexName();

        this.DYNAMIC_COLUMN_FIXED_FIELD_NAME_TO_INDEX_NAME = dynamicColumnAble.getFixedColumnFieldNameToIndexName();
        this.DYNAMIC_COLUMN_DYNAMIC_FIELD_NAME_TO_INDEX_NAME = dynamicColumnAble.getDynamicColumnFieldName();

        this.fixedColumnVOClass = fixedColumnAble.getClass();
        this.dynamicColumnVOClass = dynamicColumnAble.getClass();
    }

    /**
     * 解析表头（只执行一次）
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        // 遍历表头，初始化索引映射
        headMap.forEach((index, cellData) -> {
            String columnName = cellData.trim();
            ALL_COLUMN_INDEX_VALUE_TO_HEAD.put(index, columnName);

            // 缓存固定列索引
            if (FIXED_COLUMNS.contains(columnName)) {
                String indexName = FIXED_COLUMN_HEAD_TO_INDEX_NAME.get(columnName);
                FIXED_COLUMN_INDEX_NAME_TO_INDEX_VALUE.put(indexName, index);
            }

            // 收集动态列索引（非固定列）
            if (!FIXED_COLUMNS.contains(columnName)) {
                DYNAMIC_COLUMN_INDEX_NAME.add(index);
            }
        });

        // 校验固定列是否存在
        validateFixedColumns();
    }

    /**
     * 校验固定列是否存在，不存在则抛出异常
     */
    private void validateFixedColumns() {
        List<String> missingColumns = new ArrayList<>();
        FIXED_COLUMN_INDEX_NAME_TO_INDEX_VALUE.forEach((indexName, indexValue) -> {
            if (indexValue == -1) missingColumns.add(FIXED_COLUMN_INDEX_NAME_TO_HEAD.get(indexName));
        });

        if (!missingColumns.isEmpty()) {
            throw new IllegalArgumentException("Excel缺少必填固定列：" + String.join(",", missingColumns));
        }
    }

    /**
     * 解析每行数据（核心逻辑）
     */
    @Override
    public void invoke(Map<Integer, String> rowData, AnalysisContext context) {
        // 跳过空行（所有列都为空）
        if (rowData.isEmpty() || isAllCellEmpty(rowData)) {
            return;
        }

        System.out.println("开始解析第" + context.readRowHolder().getRowIndex() + "行");

        // 1. 解析固定列数据
        @SuppressWarnings("unchecked")
        T fixedVO = (T) BeanUtils.instantiateClass(fixedColumnVOClass);
        FIXED_COLUMN_FIELD_NAME_TO_INDEX_NAME.forEach((key, value) ->
                FastFieldSetter.setFieldValue(fixedVO, key, getCellValue(rowData, FIXED_COLUMN_INDEX_NAME_TO_INDEX_VALUE.get(value))));

        // 2. 解析动态列数据（每个动态列都关联项目、阶段）
        List<D> dynamicVOList = new ArrayList<>();
        for (Integer dynamicIndex : DYNAMIC_COLUMN_INDEX_NAME) {
            // 获取动态列名称和值
            String columnName = ALL_COLUMN_INDEX_VALUE_TO_HEAD.get(dynamicIndex);
            String columnValue = getCellValue(rowData, dynamicIndex);

            // 跳过空值的动态列（可选，根据业务调整）
            if (columnValue.isEmpty()) {
                continue;
            }

            // 构建动态列VO，关联项目、阶段
            @SuppressWarnings("unchecked")
            D dynamicVO = (D) BeanUtils.instantiateClass(dynamicColumnVOClass);

            DYNAMIC_COLUMN_FIXED_FIELD_NAME_TO_INDEX_NAME.forEach((key, value) -> {
                System.out.println("开始处理动态列 key: " + key + " value：" + value);
                if (StringUtils.endsWithIgnoreCase(key, "prototypeNumber")) {
                    System.out.println();
                }
                FastFieldSetter.setFieldValue(dynamicVO, key, getCellValue(rowData, FIXED_COLUMN_INDEX_NAME_TO_INDEX_VALUE.get(value)));
            });

            DYNAMIC_COLUMN_DYNAMIC_FIELD_NAME_TO_INDEX_NAME.forEach((key, value) -> {
                if (key.equals("head")) {
                    FastFieldSetter.setFieldValue(dynamicVO, value, columnName);
                } else if (key.equals("value")) {
                    FastFieldSetter.setFieldValue(dynamicVO, value, columnValue);
                }
            });

            dynamicVOList.add(dynamicVO);
        }

        // 3. 组装当前行的完整数据
        ExcelDataVO<T, D> excelDataVO = new ExcelDataVO<>();
        excelDataVO.setFixedColumn(fixedVO);
        excelDataVO.setDynamicColumns(dynamicVOList);
        excelDataVO.setRowNum(context.readRowHolder().getRowIndex());
        resultList.add(excelDataVO);
    }

    /**
     * 判断行数据是否全为空
     */
    private boolean isAllCellEmpty(Map<Integer, String> rowData) {
        return rowData.values().stream().allMatch(val -> val == null || val.trim().isEmpty());
    }

    /**
     * 安全获取单元格值（空值返回空字符串）
     */
    private String getCellValue(Map<Integer, String> rowData, int index) {
        return Optional.ofNullable(rowData.get(index)).map(String::trim).orElse("");
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("Excel解析完成，共读取有效行：" + resultList.size());
        System.out.println("Excel解析完成，读取数据如下：" + JSONUtil.toJsonPrettyStr(resultList));
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        System.err.println("Excel解析异常：" + exception.getMessage());
        throw exception; // 抛出异常，让上层处理
    }

}

