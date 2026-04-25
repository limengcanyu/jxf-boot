package org.asura.fastexcel.dto;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 导出数据实体（示例：订单数据）
 */
@Data
public class OrderExportDTO {
    @ExcelProperty("订单ID")
    private Long orderId;

    @ExcelProperty("用户ID")
    private Long userId;

    @ExcelProperty("订单金额")
    private String amount;

    @ExcelProperty("创建时间")
    private String createTime;

    // 更多字段根据实际业务补充
}

