package com.spring.boot.fastexcel.controller;


import cn.hutool.json.JSONUtil;
import cn.idev.excel.FastExcelFactory;
import com.spring.boot.fastexcel.utils.DynamicExcelReaderUtil;
import com.spring.boot.fastexcel.utils.ExcelDataGenericAnalysisEventListener;
import com.spring.boot.fastexcel.validator.GenericExcelDataValidator;
import com.spring.boot.fastexcel.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel上传与解析控制器
 */
@Slf4j
@RestController
public class ExcelUploadController {

//    @Autowired
//    private ExcelDataValidator excelDataValidator;

    /**
     * http://localhost:8080/api/excel/hello
     *
     * @return
     */
    @GetMapping("/api/excel/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.badRequest().body("hello");
    }

    /**
     * http://localhost:8080/api/excel/upload
     *
     * @return
     */
    @PostMapping("/api/excel/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.badRequest().body("hello");
    }

    /**
     * http://localhost:8080/api/excel/upload
     * <p>
     * 上传Excel并解析动态列数据
     *
     * @param file Excel文件（支持.xls/.xlsx）
     * @return 解析结果
     */
    @PostMapping("/api/excel/uploadAndParseExcel")
    public ResponseEntity<?> uploadAndParseExcel(@RequestParam("file") MultipartFile file) {
        // 1. 基础校验
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("错误：上传的文件不能为空");
        }

        // 2. 校验文件格式
        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
            return ResponseEntity.badRequest().body("错误：仅支持上传Excel格式文件（.xls/.xlsx）");
        }

        // 3. 解析Excel
        List<ExcelDataVO<ExcelFixedColumnVO, ExcelDynamicColumnVO>> excelDataList = new ArrayList<>();

        try {
            // 使用EasyExcel的SAX模式读取（避免大文件内存溢出）
            FastExcelFactory.read(
                            file.getInputStream(),
                            new ExcelDataGenericAnalysisEventListener<>(
                                    excelDataList,
                                    new ExcelFixedColumnVO(),
                                    new ExcelDynamicColumnVO()
                            )
                    ).sheet("Sheet1") // 指定读取sheet1
                    .headRowNumber(1) // 表头行号（默认第1行，根据实际调整）
                    .doRead();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("解析失败：" + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件读取异常：" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("解析异常：" + e.getMessage());
        }

        // 3. 数据校验
        try {
            GenericExcelDataValidator.validateAndThrow(excelDataList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        // 4. 校验通过，返回结果
        return ResponseEntity.ok().body("解析并校验成功！共读取" + excelDataList.size() + "行有效数据：" + JSONUtil.toJsonPrettyStr(excelDataList));
    }

    /**
     * http://localhost:8080/api/excel/uploadFullConfigBom
     * <p>
     * 上传Excel并解析动态列数据
     *
     * @param file Excel文件（支持.xls/.xlsx）
     * @return 解析结果
     */
    @PostMapping("/api/excel/uploadFullConfigBom")
    public ResponseEntity<?> uploadFullConfigBom(@RequestParam("file") MultipartFile file) {
        // 1. 基础校验
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("错误：上传的文件不能为空");
        }

        // 2. 校验文件格式
        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
            return ResponseEntity.badRequest().body("错误：仅支持上传Excel格式文件（.xls/.xlsx）");
        }

        // 3. 解析Excel
        List<ExcelDataVO<OrderExcelFixedColumnVO, OrderExcelDynamicColumnVO>> excelDataList = new ArrayList<>();

        try {
            excelDataList = DynamicExcelReaderUtil.readFullConfigBom(file);
        } catch (IllegalArgumentException e) {
            log.error("解析失败：", e);
            return ResponseEntity.badRequest().body("解析失败：" + e.getMessage());
        } catch (IOException e) {
            log.error("文件读取异常：", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件读取异常：" + e.getMessage());
        } catch (Exception e) {
            log.error("解析异常：", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("解析异常：" + e.getMessage());
        }

        // 3. 数据校验
        try {
            // 模拟动态列校验错误
            excelDataList.forEach(excelData -> {
                List<OrderExcelDynamicColumnVO> excelDynamicColumnVOList = excelData.getDynamicColumns();
                for (OrderExcelDynamicColumnVO orderExcelDynamicColumnVO : excelDynamicColumnVOList) {
                    orderExcelDynamicColumnVO.setPartNumber(null);
                }
            });

            GenericExcelDataValidator.validateAndThrow(excelDataList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        // 4. 校验通过，返回结果
        return ResponseEntity.ok().body("解析并校验成功！共读取" + excelDataList.size() + "行有效数据：" + JSONUtil.toJsonPrettyStr(excelDataList));
    }

}

