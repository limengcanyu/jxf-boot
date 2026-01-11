package com.spring.boot.fastexcel.controller;


import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcelFactory;
import cn.idev.excel.converters.longconverter.LongStringConverter;
import cn.idev.excel.write.metadata.WriteSheet;
import com.spring.boot.fastexcel.dto.OrderExportDTO;
import com.spring.boot.fastexcel.service.OrderService;
import com.spring.boot.fastexcel.utils.ExcelBigDataExportUtil;
import com.spring.boot.fastexcel.utils.HttpUtils;
import com.spring.boot.fastexcel.vo.DynamicFieldObjectPageReqVO;
import com.spring.boot.fastexcel.vo.DynamicFieldObjectRespVO;
import com.spring.boot.fastexcel.vo.FixedFieldObjectRespVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
        // 设置 header 和 contentType
        response.addHeader("Content-Disposition", "attachment;filename=" + HttpUtils.encodeUtf8("动态导出.xlsx"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");

        List<DynamicFieldObjectRespVO> biwSysVehicleBomPageReqVOList = DynamicFieldObjectRespVO.getBiwSysVehicleBomRespVOList();
        List<String> dynamicFieldList = DynamicFieldObjectRespVO.getDynamicFieldList(biwSysVehicleBomPageReqVOList);
        List<List<String>> head = DynamicFieldObjectRespVO.getHead(dynamicFieldList);
        List<List<Object>> data = DynamicFieldObjectRespVO.getData(dynamicFieldList, biwSysVehicleBomPageReqVOList);
        System.out.println();

        try (ExcelWriter excelWriter = FastExcelFactory.write(response.getOutputStream())
                .autoCloseStream(false)
//                .registerWriteHandler(new ColumnWidthMatchStyleStrategy())
                .registerConverter(new LongStringConverter())
                .build()) {
            WriteSheet sheet1 = FastExcelFactory.writerSheet("sheet1")
                    .head(head)
                    .build();
            excelWriter.write(data, sheet1);

            WriteSheet sheet2 = FastExcelFactory.writerSheet("sheet2")
                    .head(FixedFieldObjectRespVO.class)
                    .build();
            excelWriter.write(FixedFieldObjectRespVO.getFixedFieldObjectRespVOList(), sheet2);
        } catch (Exception e) {
            throw new RuntimeException("Excel导出失败：" + e.getMessage(), e);
        }
    }

    @Resource
    private OrderService orderService; // 业务服务层

    /**
     * 导出100M级订单数据（示例）
     */
    @GetMapping("/order/bigData")
    public void exportBigOrderData(HttpServletResponse response) {
        // 配置：每批查询2000行，平衡查询效率和内存占用
        int pageSize = 2000;

        // 调用工具类导出
        ExcelBigDataExportUtil.exportBigData(
                response,
                "订单数据-全量",
                (pageNum, size) -> orderService.queryOrderByPage(pageNum, size), // 分批查询数据
                pageSize,
                OrderExportDTO.class
        );
    }
}

