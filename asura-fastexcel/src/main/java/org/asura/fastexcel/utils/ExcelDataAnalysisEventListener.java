package org.asura.fastexcel.utils;


import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import org.asura.fastexcel.vo.ExcelDataVO;
import org.asura.fastexcel.vo.ExcelDynamicColumnVO;
import org.asura.fastexcel.vo.ExcelFixedColumnVO;

import java.util.*;

/**
 * Excel数据解析类
 */
public class ExcelDataAnalysisEventListener extends AnalysisEventListener<Map<Integer, String>> {
    // 固定列名称（需与Excel列名完全一致，区分大小写）
    private static final String COLUMN_PROJECT = "项目";
    private static final String COLUMN_STAGE = "阶段";
    private static final String COLUMN_IDENTIFIER = "标识";
    private static final Set<String> FIXED_COLUMNS = new HashSet<>(Arrays.asList(
            COLUMN_PROJECT, COLUMN_STAGE, COLUMN_IDENTIFIER
    ));

    // 表头映射：key=列索引，value=列名称
    private final Map<Integer, String> headIndexToName = new LinkedHashMap<>();
    // 固定列索引缓存（项目、阶段、标识）
    private int projectColumnIndex = -1;
    private int stageColumnIndex = -1;
    private int identifierColumnIndex = -1;
    // 动态列索引列表（排除固定列）
    private final List<Integer> dynamicColumnIndexes = new ArrayList<>();
    // 存储最终解析结果
    List<ExcelDataVO> resultList = new ArrayList<>();

    /**
     * 解析表头（只执行一次）
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        // 遍历表头，初始化索引映射 索引从0开始
        headMap.forEach((index, cellData) -> {
            String columnName = cellData.trim();
            headIndexToName.put(index, columnName);

            // 缓存固定列索引
            switch (columnName) {
                case COLUMN_PROJECT -> projectColumnIndex = index;
                case COLUMN_STAGE -> stageColumnIndex = index;
                case COLUMN_IDENTIFIER -> identifierColumnIndex = index;
            }

            // 收集动态列索引（非固定列）
            if (!FIXED_COLUMNS.contains(columnName)) {
                dynamicColumnIndexes.add(index);
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
        if (projectColumnIndex == -1) missingColumns.add(COLUMN_PROJECT);
        if (stageColumnIndex == -1) missingColumns.add(COLUMN_STAGE);
        if (identifierColumnIndex == -1) missingColumns.add(COLUMN_IDENTIFIER);

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

        // 1. 解析固定列数据
        ExcelFixedColumnVO fixedVO = new ExcelFixedColumnVO();
        // 项目
        fixedVO.setProject(getCellValue(rowData, projectColumnIndex));
        // 阶段
        fixedVO.setStage(getCellValue(rowData, stageColumnIndex));
        // 标识
        fixedVO.setIdentifier(getCellValue(rowData, identifierColumnIndex));

        // 2. 解析动态列数据（每个动态列都关联项目、阶段）
        List<ExcelDynamicColumnVO> dynamicVOList = new ArrayList<>();
        for (Integer dynamicIndex : dynamicColumnIndexes) {
            // 获取动态列名称和值
            String columnName = headIndexToName.get(dynamicIndex);
            String columnValue = getCellValue(rowData, dynamicIndex);

            // 跳过空值的动态列（可选，根据业务调整）
            if (columnValue.isEmpty()) {
                continue;
            }

            // 构建动态列VO，关联项目、阶段
            ExcelDynamicColumnVO dynamicVO = new ExcelDynamicColumnVO();
            dynamicVO.setProject(fixedVO.getProject()); // 关联项目
            dynamicVO.setStage(fixedVO.getStage());     // 关联阶段
            dynamicVO.setColumnName(columnName);       // 动态列名称
            dynamicVO.setColumnValue(columnValue);     // 动态列值
            dynamicVOList.add(dynamicVO);
        }

        // 3. 组装当前行的完整数据
        ExcelDataVO excelDataVO = new ExcelDataVO();
        // 设置行号（context.readRowHolder().getRowIndex() 是Excel的实际行号）
        excelDataVO.setRowNum(context.readRowHolder().getRowIndex());
        excelDataVO.setFixedColumn(fixedVO);
        excelDataVO.setDynamicColumns(dynamicVOList);

//                        // 校验当前行数据
//                        ExcelDataValidator.validateAndThrow(excelDataVO);

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
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        System.err.println("Excel解析异常：" + exception.getMessage());
        throw exception; // 抛出异常，让上层处理
    }
}

