package com.spring.boot.fastexcel.utils;


import cn.idev.excel.EasyExcel;
import cn.idev.excel.ExcelWriter;
import cn.idev.excel.write.builder.ExcelWriterBuilder;
import cn.idev.excel.write.metadata.WriteSheet;
import cn.idev.excel.write.metadata.style.WriteCellStyle;
import cn.idev.excel.write.style.HorizontalCellStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * EasyExcel 大数据导出工具类
 */
public class ExcelBigDataExportUtil {

    /**
     * 构建通用的 Excel 写入配置（关闭样式缓存，提升性能）
     */
    private static ExcelWriterBuilder getExcelWriterBuilder(HttpServletResponse response, String fileName, Class<?> clazz) throws IOException {
        // 响应头配置
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 防止中文文件名乱码
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");

        // 简单样式（避免复杂样式消耗内存）
        WriteCellStyle contentStyle = new WriteCellStyle();
        contentStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        HorizontalCellStyleStrategy styleStrategy = new HorizontalCellStyleStrategy(null, contentStyle);

        // 核心配置：关闭自动头、样式缓存，提升写入效率
        return EasyExcel.write(response.getOutputStream(), clazz)
                .autoCloseStream(true) // 自动关闭输出流
                .registerWriteHandler(styleStrategy)
                .useDefaultStyle(false) // 禁用默认样式，减少内存占用
                .inMemory(false); // 禁用内存模式（关键：流式写入）
    }

    /**
     * 分批导出大数据（核心方法）
     * @param response HTTP响应
     * @param fileName 导出文件名
     * @param queryFunc 数据查询函数（参数：页码、页大小，返回：当前页数据）
     * @param pageSize 每批查询的行数（建议1000-5000，根据内存调整）
     * @param <T> 数据类型
     */
    public static <T> void exportBigData(HttpServletResponse response,
                                         String fileName,
                                         java.util.function.BiFunction<Integer, Integer, List<T>> queryFunc,
                                         int pageSize,
                                         Class<T> clazz) {
        ExcelWriterBuilder writerBuilder = null;
        try {
            writerBuilder = getExcelWriterBuilder(response, fileName, clazz);
            ExcelWriter excelWriter = writerBuilder.build();

            // 初始化Sheet（支持分Sheet：当单Sheet行数超1048576时，新建Sheet）
            int sheetNo = 0;
            int rowCount = 0;
            int pageNum = 1;
            final int MAX_ROWS_PER_SHEET = 1048576; // Excel单Sheet最大行数

            while (true) {
                // 分批查询数据（核心：避免一次性加载全量数据）
                List<T> data = queryFunc.apply(pageNum, pageSize);
                if (data == null || data.isEmpty()) {
                    break; // 无数据则退出循环
                }

                // 检查是否需要新建Sheet（单Sheet行数超限）
                if (rowCount >= MAX_ROWS_PER_SHEET) {
                    sheetNo++;
                    rowCount = 0;
                }

                // 构建Sheet
                WriteSheet writeSheet = EasyExcel.writerSheet(sheetNo, "数据-" + (sheetNo + 1)).build();

                // 写入当前批数据（流式写入，不缓存）
                excelWriter.write(data, writeSheet);

                // 更新计数
                rowCount += data.size();
                pageNum++;

                // 手动释放当前批数据的内存（可选：建议添加，减少GC压力）
                data.clear();
                System.gc(); // 触发轻量GC（根据实际情况调整）
            }

            // 完成写入
            excelWriter.finish();
        } catch (Exception e) {
            throw new RuntimeException("大数据导出失败", e);
        }
    }
}

