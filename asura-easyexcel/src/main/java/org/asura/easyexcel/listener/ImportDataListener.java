package org.asura.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.asura.easyexcel.dto.ImportDataDTO;
import org.asura.easyexcel.service.SaveDataService;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel数据读取监听器（批次处理）
 */
@Slf4j
public class ImportDataListener extends AnalysisEventListener<ImportDataDTO> {

    // 批次大小（可配置化，建议500-1000条/批）
    private static final int BATCH_SIZE = 1000;
    // 临时存储当前批次数据
    private final List<ImportDataDTO> batchDataList = new ArrayList<>(BATCH_SIZE);
    // 数据处理服务（实际使用时通过构造器注入）
    private final SaveDataService saveDataService;
    // 用户ID（用于隔离临时目录和日志）
    private final String userId;

    public ImportDataListener(SaveDataService saveDataService, String userId) {
        this.saveDataService = saveDataService;
        this.userId = userId;
    }

    /**
     * 每读取一行数据触发
     */
    @Override
    public void invoke(ImportDataDTO data, AnalysisContext context) {
        batchDataList.add(data);
        // 达到批次大小则处理
        if (batchDataList.size() >= BATCH_SIZE) {
            processBatchData();
            // 清空批次数据
            batchDataList.clear();
        }
    }

    /**
     * 所有数据读取完成后触发（处理剩余数据）
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (!batchDataList.isEmpty()) {
            processBatchData();
        }
        log.info("用户{}：Excel数据读取完成，已处理所有批次", userId);
    }

    /**
     * 处理单批次数据
     */
    private void processBatchData() {
        log.info("用户{}：开始处理批次数据，本次批次大小：{}", userId, batchDataList.size());
        // 调用业务服务处理数据（实际入库逻辑）
        saveDataService.saveBatchData(batchDataList, userId);
    }
}
