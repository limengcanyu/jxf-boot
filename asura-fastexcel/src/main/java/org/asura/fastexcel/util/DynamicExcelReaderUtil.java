package org.asura.fastexcel.util;


import cn.idev.excel.FastExcelFactory;
import org.asura.fastexcel.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel动态列读取工具类（动态列关联项目、阶段）
 */
public class DynamicExcelReaderUtil {

    /**
     * 读取Excel（sheet1）的动态列数据
     *
     * @param file 上传的Excel文件
     * @return 解析后的完整数据列表
     * @throws IOException 文件读取异常
     */
    public static List<ExcelDataVO<ExcelFixedColumnAble, ExcelDynamicColumnAble>> readDynamicExcel(MultipartFile file) throws IOException {
        // 存储最终解析结果
        List<ExcelDataVO<ExcelFixedColumnAble, ExcelDynamicColumnAble>> resultList = new ArrayList<>();

        // 使用EasyExcel的SAX模式读取（避免大文件内存溢出）
        FastExcelFactory.read(
                        file.getInputStream(),
                        new ExcelDataGenericAnalysisEventListener<>(
                                resultList,
                                new ExcelFixedColumnVO(),
                                new ExcelDynamicColumnVO()
                        )
                ).sheet("Sheet1") // 指定读取sheet1
                .headRowNumber(1) // 表头行号（默认第1行，根据实际调整）
                .doRead();

        return resultList;
    }

    /**
     * 读取Excel（sheet1）的动态列数据
     *
     * @param file 上传的Excel文件
     * @return 解析后的完整数据列表
     * @throws IOException 文件读取异常
     */
    public static List<ExcelDataVO<OrderExcelFixedColumnVO, OrderExcelDynamicColumnVO>> readFullConfigBom(MultipartFile file) throws IOException {
        // 存储最终解析结果
        List<ExcelDataVO<OrderExcelFixedColumnVO, OrderExcelDynamicColumnVO>> resultList = new ArrayList<>();

        // 使用EasyExcel的SAX模式读取（避免大文件内存溢出）
        FastExcelFactory.read(
                        file.getInputStream(),
                        new ExcelDataGenericAnalysisEventListener<>(
                                resultList,
                                new OrderExcelFixedColumnVO(),
                                new OrderExcelDynamicColumnVO()
                        )
                ).sheet("Sheet1") // 指定读取sheet1
                .headRowNumber(1) // 表头行号（默认第1行，根据实际调整）
                .doRead();

        return resultList;
    }
}
