package org.asura.easyexcel.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.alibaba.excel.write.metadata.WriteSheet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.asura.common.util.HttpUtils;
import org.asura.common.vo.DynamicFieldObjectPageReqVO;
import org.asura.common.vo.DynamicFieldObjectRespVO;
import org.asura.easyexcel.vo.FixedFieldObjectRespVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequestMapping("/export")
@RestController
public class ExportController {

    /**
     * http://localhost:8080/export/hello
     */
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    /**
     * http://localhost:8080/export/export-excel
     */
    @GetMapping("/export-excel")
    public void exportBiwSysVehicleBomExcel(@Valid DynamicFieldObjectPageReqVO pageReqVO,
                                            HttpServletResponse response) throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateTime = dateTimeFormatter.format(LocalDateTime.now());

        // 设置 header 和 contentType
        response.addHeader("Content-Disposition", "attachment;filename=" + HttpUtils.encodeUtf8("动态导出") + dateTime + ".xlsx");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");

        List<DynamicFieldObjectRespVO> biwSysVehicleBomPageReqVOList = DynamicFieldObjectRespVO.getBiwSysVehicleBomRespVOList();
        List<String> dynamicFieldList = DynamicFieldObjectRespVO.getDynamicFieldList(biwSysVehicleBomPageReqVOList);
        List<List<String>> head = DynamicFieldObjectRespVO.getHead(dynamicFieldList);
        List<List<Object>> data = DynamicFieldObjectRespVO.getData(dynamicFieldList, biwSysVehicleBomPageReqVOList);
        System.out.println();

        try (ExcelWriter excelWriter = EasyExcelFactory.write(response.getOutputStream())
                .autoCloseStream(false)
//                .registerWriteHandler(new ColumnWidthMatchStyleStrategy())
                .registerConverter(new LongStringConverter())
                .build()) {
            WriteSheet sheet1 = EasyExcelFactory.writerSheet("sheet1")
                    .head(head)
                    .build();
            excelWriter.write(data, sheet1);

            WriteSheet sheet2 = EasyExcelFactory.writerSheet("sheet2")
                    .head(FixedFieldObjectRespVO.class)
                    .build();
            excelWriter.write(FixedFieldObjectRespVO.getFixedFieldObjectRespVOList(), sheet2);
        } catch (Exception e) {
            throw new RuntimeException("Excel导出失败：" + e.getMessage(), e);
        }
    }

}
