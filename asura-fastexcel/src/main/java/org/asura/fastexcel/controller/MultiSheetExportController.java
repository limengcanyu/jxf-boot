package org.asura.fastexcel.controller;


import org.asura.fastexcel.dto.OrderExportDTO;
import org.asura.fastexcel.dto.UserExportDTO;
import org.asura.fastexcel.service.OrderService;
import org.asura.fastexcel.service.UserService;
import org.asura.fastexcel.utils.MultiSheetBigDataExportUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/export")
public class MultiSheetExportController {

    @Resource
    private OrderService orderService;
    @Resource
    private UserService userService;

    /**
     * 多线程导出两个Sheet的大数据
     */
    @GetMapping("/multiSheet/bigData")
    public void exportMultiSheetBigData(HttpServletResponse response) {
        // 每批查询行数（建议1000-5000）
        int pageSize = 2000;

        // 调用多Sheet导出工具
        MultiSheetBigDataExportUtil.exportMultiSheet(
                response,
                "订单+用户全量数据",
                "订单数据",
                OrderExportDTO.class,
                // Sheet1数据生产者：分批查询订单数据并放入队列
                (queue, size) -> {
                    int pageNum = 1;
                    while (true) {
                        List<OrderExportDTO> data = orderService.queryOrderByPage(pageNum, size);
                        if (data == null || data.isEmpty()) {
                            break;
                        }
                        // 数据放入队列（阻塞，直到队列有空间）
                        queue.put(data);
                        pageNum++;
                    }
                },
                "用户数据",
                UserExportDTO.class,
                // Sheet2数据生产者：分批查询用户数据并放入队列
                (queue, size) -> {
                    int pageNum = 1;
                    while (true) {
                        List<UserExportDTO> data = userService.queryUserByPage(pageNum, size);
                        if (data == null || data.isEmpty()) {
                            break;
                        }
                        // 数据放入队列（阻塞，直到队列有空间）
                        queue.put(data);
                        pageNum++;
                    }
                },
                pageSize
        );
    }
}

