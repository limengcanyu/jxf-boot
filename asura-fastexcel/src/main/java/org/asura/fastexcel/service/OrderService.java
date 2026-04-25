package org.asura.fastexcel.service;

import org.asura.fastexcel.dto.OrderExportDTO;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    /**
     * 分批查询订单数据（MyBatis示例）
     */
    public List<OrderExportDTO> queryOrderByPage(Integer pageNum, Integer pageSize) {
        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;
        // MyBatis mapper：分页查询（避免使用SELECT *，只查需要的字段）
        return new ArrayList<>();
    }
}

